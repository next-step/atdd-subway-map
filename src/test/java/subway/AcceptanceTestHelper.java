package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class AcceptanceTestHelper {
    static ExtractableResponse<Response> 지하철역_등록(final String name) {
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
}
