package subway.acceptancetest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class AcceptanceTestHelper {
    static ExtractableResponse<Response> 지하철역_생성(final String name) {
        final Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return RestAssured
                .given().log().all().body(params).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract();
    }

    static ExtractableResponse<Response> 지하철역_목록_조회() {
        return RestAssured.
                given().log().all()
                .when().get("/stations")
                .then().log().all()
                .extract();
    }

    static ExtractableResponse<Response> 지하철역_삭제(final long id) {
        return RestAssured.
                given().log().all()
                .when().delete("/stations/" + id)
                .then().log().all()
                .extract();
    }

    static ExtractableResponse<Response> 지하철노선_생성(final String name, final String color,
                                                  final Long upStationId, final Long downStationId,
                                                  final int distance) {
        final Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        return RestAssured
                .given().log().all().body(params).contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all()
                .extract();
    }

    static ExtractableResponse<Response> 지하철노선_목록_조회() {
        return RestAssured
                .given().log().all().accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines")
                .then().log().all()
                .extract();
    }

    static ExtractableResponse<Response> 지하철노선_조회(final Long id) {
        return RestAssured
                .given().log().all().accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/lines/" + id)
                .then().log().all()
                .extract();
    }

    static ExtractableResponse<Response> 지하철노선_수정(final Long id, final String name, final String color) {
        final Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        return RestAssured
                .given().log().all().body(params).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/" + id)
                .then().log().all()
                .extract();
    }

    static ExtractableResponse<Response> 지하철노선_삭제(final Long id) {
        return RestAssured
                .given().log().all()
                .when().delete("/lines/" + id)
                .then().log().all()
                .extract();
    }

    static ExtractableResponse<Response> 지하철구간_생성(final Long lineId, final Long upStationId,
                                                  final Long downStationId, final int distance) {
        final Map<String, Object> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        return RestAssured
                .given().log().all().body(params).contentType(MediaType.APPLICATION_JSON_VALUE + "; charset=UTF-8")
                .when().post("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();
    }

    static ExtractableResponse<Response> 지하철구간_삭제(final Long lineId, final Long stationId) {
        final Map<String, Object> params = new HashMap<>();
        params.put("stationId", stationId);
        return RestAssured
                .given().log().all().params(params)
                .when().delete("/lines/" + lineId + "/sections")
                .then().log().all()
                .extract();
    }

    static Long 아이디_추출(final ExtractableResponse<Response> response) {
        return response.body().jsonPath().getLong("id");
    }

    static void 상태_코드_확인(final ExtractableResponse<Response> response, final int statusCode) {
        assertThat(response.statusCode()).isEqualTo(statusCode);
    }
}
