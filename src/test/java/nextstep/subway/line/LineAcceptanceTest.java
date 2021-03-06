package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "선릉");
        params.put("color", "green darken-1");
        // when
        // 지하철_노선_생성_요청
        ExtractableResponse<Response> response = RestAssured.given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
                .when()
                .post("/lines")
                .then().log().all().extract();

        // then
        // 지하철_노선_생성됨
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        // 지하철_노선_등록되어_있음
        // 지하철_노선_등록되어_있음
        Map<String, String> params1 = new HashMap<>();
        params1.put("name", "선릉");
        params1.put("color", "green darken-1");

        ExtractableResponse<Response> createResponse1 = RestAssured.given()
                .body(params1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
                .when()
                .post("/lines")
                .then().log().all().extract();


        Map<String, String> params2 = new HashMap<>();
        params2.put("name", "강남");
        params2.put("color", "green darken-1");

        ExtractableResponse<Response> createResponse2 = RestAssured.given()
                .body(params2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
                .when()
                .post("/lines")
                .then().log().all().extract();

        // when
        // 지하철_노선_목록_조회_요청
        ExtractableResponse<Response> getResponses = RestAssured.given()
                .log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all().extract();

        // then
        // 지하철_노선_목록_응답됨
        // 지하철_노선_목록_포함됨
        assertThat(getResponses.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<LineResponse> lineResponses = getResponses.jsonPath().getList(".", LineResponse.class);
        assertThat(lineResponses.size()).isEqualTo(2);

        List<Long> expectedLineIds = Arrays.asList(createResponse1, createResponse2).stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());
        List<Long> resultLineIds = getResponses.jsonPath().getList(".", LineResponse.class).stream()
                .map(it -> it.getId())
                .collect(Collectors.toList());
        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        // 지하철_노선_등록되어_있음
        Map<String, String> params1 = new HashMap<>();
        params1.put("name", "선릉");
        params1.put("color", "green darken-1");

        ExtractableResponse<Response> createResponse =
                RestAssured.given()
                .body(params1)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
                .when()
                .post("/lines")
                .then().log().all().extract();

        Long id = Long.parseLong(
                createResponse
                        .header("Location")
                        .split("/")[2]
        );

        // when
        // 지하철_노선_조회_요청
        ExtractableResponse<Response> getResponse = RestAssured.given().log().all()
        .accept(MediaType.APPLICATION_JSON_VALUE)
        .when().get("/lines/" + id)
        .then().log().all().extract();

        // then
        // 지하철_노선_응답됨
        assertThat(getResponse.statusCode())
                .isEqualTo(HttpStatus.OK.value());
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        // 지하철_노선_등록되어_있음
        Map<String, String> params1 = new HashMap<>();
        params1.put("name", "선릉");
        params1.put("color", "green darken-1");

        ExtractableResponse<Response> createResponse =
                RestAssured.given()
                        .body(params1)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .log().all()
                        .when()
                        .post("/lines")
                        .then().log().all().extract();

        Long id = Long.parseLong(
                createResponse
                        .header("Location")
                        .split("/")[2]
        );

        // when
        // 지하철_노선_수정_요청
        Map<String, String> params2 = new HashMap<>();
        params1.put("name", "선정릉");
        params1.put("color", "red darken-1");

        ExtractableResponse<Response> updateResponse = RestAssured.given()
                .body(params2)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
                .when().put("/lines/" + id)
                .then().log().all().extract();

        // then
        // 지하철_노선_수정됨
        assertThat(updateResponse.jsonPath().getMap("name")).isEqualTo(params2.get("name"));
        assertThat(updateResponse.jsonPath().getMap("color")).isEqualTo(params2.get("color"));

    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        // 지하철_노선_등록되어_있음
        Map<String, String> params = new HashMap<>();
        params.put("name", "선릉");
        params.put("color", "green darken-1");

        ExtractableResponse<Response> createResponse = RestAssured.given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .log().all()
                .when()
                .post("/lines")
                .then().log().all().extract();

        Long id = Long.parseLong(
                createResponse
                        .header("Location")
                        .split("/")[2]
        );
        // when
        // 지하철_노선_제거_요청
        ExtractableResponse<Response> deleteResponse = RestAssured.given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/lines/" + id)
                .then().log().all().extract();
        // then
        // 지하철_노선_삭제됨
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
