package subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TestFixtureLine {

    static ExtractableResponse<Response> 지하철_노선_생성_요청(final String name, final String color, final Long upStationId, final Long downStationId, final int distance) {
        final Map<String, Object> params = 지하철_생성_값(name, color, upStationId,downStationId, distance);

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    static Map<String, Object> 지하철_생성_값(String name, String color, Long upStationId, Long downStationId, int distance) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        return params;
    }

    static void 지하철_노선_생성됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    static void 지하철_노선_목록_조회됨(ExtractableResponse<Response> response, final String lineName, final String lineColor, final int countOfStation) {
        final JsonPath jsonPath = response.response().body().jsonPath();

        assertAll(
                () -> assertThat(response.response().statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(jsonPath.getString("[0].name")).isEqualTo(lineName),
                () -> assertThat(jsonPath.getString("[0].color")).isEqualTo(lineColor),
                () -> assertThat(jsonPath.getList("[0].stations")).hasSize(countOfStation)
        );
    }

    static void 지하철_노선_목록_조회됨(final ExtractableResponse<Response> lineResponse) {
        final JsonPath 지하철_목록_응답_경로 = lineResponse.response().body().jsonPath();

        assertAll(
                () -> assertThat(lineResponse.response().statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(지하철_목록_응답_경로.getList("")).hasSize(2)
        );
    }

    static ExtractableResponse<Response> 지하철_노선_조회_요청(final ExtractableResponse<Response> response) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(response.header("location"))
                .then().log().all()
                .extract();
    }

    static void 지하철_노선_조회됨(ExtractableResponse<Response> response, final String lineName, final String lineColor, final int countOfStation) {
        final JsonPath jsonPath = response.response().body().jsonPath();

        assertAll(
                () -> assertThat(response.response().statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(jsonPath.getString("name")).isEqualTo(lineName),
                () -> assertThat(jsonPath.getString("color")).isEqualTo(lineColor),
                () -> assertThat(jsonPath.getList("stations")).hasSize(countOfStation)
        );
    }

    static ExtractableResponse<Response> 지하철_노선_수정_요청(final ExtractableResponse<Response> response, final String name, final String color) {
        final Map<String, Object> params = 파라미터_수정_요청(name, color);

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(response.header("location"))
                .then().log().all()
                .extract();
    }

    static Map<String, Object> 파라미터_수정_요청(final String name, final String color) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        return params;
    }

    static void 지하철_노선_수정됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    static ExtractableResponse<Response> 지하철_노선_삭제_요청(final ExtractableResponse<Response> response) {
        return RestAssured
                .given().log().all()
                .when().delete(response.header("location"))
                .then().log().all()
                .extract();
    }

    static void 지하철_노선_삭제됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
