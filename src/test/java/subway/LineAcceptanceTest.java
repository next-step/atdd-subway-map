package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import subway.line.LineRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@DisplayName("지하철 노선 관련 기능")
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
@Sql("/stations.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class LineAcceptanceTest {
    @DisplayName("지하철 노선을 생성한다")
    @Test
    void createLine() {
        //when
        ExtractableResponse<Response> response = createLineExtractableResponse(
                new LineRequest("신분당선", "bg-red-600", (long) 1, (long) 2, 10)
        );
        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.jsonPath().getString("name")).contains("신분당선");
    }

    @DisplayName("두 개의 지하철 노선을 생성하고 지하철 노선 목록 조회 한다.")
    @Test
    void findLines() {
        //given
        createLineExtractableResponse(new LineRequest("신분당선", "bg-red-600", (long) 1, (long) 2, 10));
        createLineExtractableResponse(new LineRequest("수인분당선", "bg-yellow-600", (long) 1, (long) 3, 10));

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("lines")
                .then().log().all()
                .extract();
        List<String> lineNames = response.jsonPath().getList("name");

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(lineNames).contains("신분당선", "수인분당선");
    }

    @DisplayName("id에 해당하는 지하철 노선을 조회한다.")
    @Test
    void findOneLine() {
        //given
        createLineExtractableResponse(new LineRequest("신분당선", "bg-red-600", (long) 1, (long) 2, 10));

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("lines/{id}", 1)
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("name")).contains("신분당선");
    }

    @DisplayName("id에 해당하는 지하철 노선을 수정한다")
    @Test
    void updateOneLine() {
        //given
        createLineExtractableResponse(new LineRequest("신분당선", "bg-red-600", (long) 1, (long) 2, 10));

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .body(new LineRequest("다른분당선", "bg-green-600"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("lines/{id}", 1)
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("name")).contains("다른분당선");
    }

    @DisplayName("id에 해당하는 지하철 노선을 삭제한다.")
    @Test
    void deleteOneLine() {
        //given
        createLineExtractableResponse(new LineRequest("신분당선", "bg-red-600", (long) 1, (long) 2, 10));

        //when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().delete("lines/{id}", 1)
                .then().log().all()
                .extract();

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private static ExtractableResponse<Response> createLineExtractableResponse(LineRequest lineRequest) {
        return RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }
}
