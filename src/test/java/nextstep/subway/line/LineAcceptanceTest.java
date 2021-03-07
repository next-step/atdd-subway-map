package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.support.ApiSupporter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        //Given
        Line line = new Line("1호선", "blue");

        //When
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(line)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();

        //Then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank(),
                () -> assertThat(response.jsonPath().getString("id")).isNotNull(),
                () -> assertThat(response.jsonPath().getString("name")).isEqualTo(line.getName()),
                () -> assertThat(response.jsonPath().getString("color")).isEqualTo(line.getColor()),
                () -> assertThat(response.jsonPath().getString("createdDate")).isNotNull(),
                () -> assertThat(response.jsonPath().getString("modifiedDate")).isNotNull()
        );
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        //Given
        Long createdId1 = ApiSupporter.callCreatedApi("1호선", "blue").jsonPath().getLong("id");
        Long createdId2 = ApiSupporter.callCreatedApi("2호선", "green").jsonPath().getLong("id");

        //When
        ExtractableResponse<Response> response = RestAssured
                    .given().log().all()
                    .when()
                        .get("/lines")
                    .then().log().all()
                    .extract();

        List<Long> lineIds = response.jsonPath().getList(".", LineResponse.class).stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());
        List<Long> createdIds = Arrays.asList(createdId1, createdId2).stream().collect(Collectors.toList());


        //Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        Assertions.assertThat(lineIds).containsAll(createdIds);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void findLineById() {
        //Given
        ExtractableResponse<Response> createdResponse = ApiSupporter.callCreatedApi("1호선", "blue");
        LineResponse createdLine = createdResponse.jsonPath().getObject(".", LineResponse.class);

        //When
        String uri = createdResponse.header("Location");
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when()
                    .get(uri)
                .then().log().all()
                .extract();
        LineResponse expectedLine = createdResponse.jsonPath().getObject(".", LineResponse.class);

        //Then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(expectedLine.getId()).isEqualTo(createdLine.getId()),
                () -> assertThat(expectedLine.getName()).isEqualTo(createdLine.getName()),
                () -> assertThat(expectedLine.getColor()).isEqualTo(createdLine.getColor()),
                () -> assertThat(expectedLine.getModifiedDate()).isEqualTo(createdLine.getModifiedDate()),
                () -> assertThat(expectedLine.getCreatedDate()).isEqualTo(createdLine.getCreatedDate()),
                () -> assertThat(response.jsonPath().getString("stations")).isNotNull()
        );
    }

    @DisplayName("등록되지 않는 지하철 노선을 조회한다.")
    @Test
    void notFoundLine() {
        //Given
        ExtractableResponse<Response> createdResponse = ApiSupporter.callCreatedApi("1호선", "blue");
        LineResponse createdLine = createdResponse.jsonPath().getObject(".", LineResponse.class);

        //When
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when()
                .get("/lines/"+createdLine.getId()+1)
                .then().log().all()
                .extract();

        //Then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value())
        );

    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        //Given
        String station = "1호선";
        String color = "blue";

        ExtractableResponse<Response> createdResponse = ApiSupporter.callCreatedApi(station, color);
        String uri = createdResponse.header("Location");

        //When
        String updatePostFix = "-600";
        LineRequest updateRequest = new LineRequest(station, color + updatePostFix);
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                    .body(updateRequest)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().
                    put(uri)
                .then().log().all()
                .extract();
        LineResponse updatedResponse = ApiSupporter.callFindApi(uri).jsonPath().getObject(".", LineResponse.class);

        //Then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(updatedResponse.getColor()).isEqualTo(color+updatePostFix)
        );
    }

    @DisplayName("등록되지 않는 노선에 수정을 요청한다")
    @Test
    void notFoundUpdateLine() {
        //Given
        String station = "1호선";
        String color = "blue";
        LineResponse lineResponse = ApiSupporter.callCreatedApi(station, color).jsonPath().getObject(".", LineResponse.class);

        //When
        String updatePostFix = "-600";
        LineRequest updateRequest = new LineRequest(station, color + updatePostFix);
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .body(updateRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().
                        put("/lines/"+lineResponse.getId()+1)
                .then().log().all()
                .extract();

        //Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        //Given
        String createdUri = ApiSupporter.callCreatedApi("1호선", "blue")
                                        .header("Location");

        //When
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when()
                    .delete(createdUri)
                .then().log().all()
                .extract();

        //Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
