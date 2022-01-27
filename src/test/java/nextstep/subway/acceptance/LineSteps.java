package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class LineSteps {

    public static String 지하철_노선이_생성되어_있음(final String name, final String color,
                                         final String upStationId, final String downStationId, final int distance) {
        return 지하철_노선_생성을_요청한다(name, color, upStationId, downStationId, distance).jsonPath().get("id").toString();
    }

    public static ExtractableResponse<Response> 지하철_노선_생성을_요청한다(final String name, final String color,
                                                                final String upStationId, final String downStationId, final int distance) {
        final Map<String, String> params = 지하철_노선_생성_데이터를_만든다(name, color, upStationId, downStationId, distance);
        return RestAssured.given().log().all()
                .body(params)
                .accept(ContentType.ANY)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    private static Map<String, String> 지하철_노선_생성_데이터를_만든다(final String name, final String color,
                                                          final String upStationId, final String downStationId, final int distance) {
        final Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", String.valueOf(distance));
        return params;
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회를_요청한다() {
        return RestAssured.given().log().all()
                .accept(ContentType.JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회를_요청한다(final String uri) {
        return RestAssured.given().log().all()
                .accept(ContentType.JSON)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_변경을_요청한다(final String uri) {
        final Map<String, String> params = 지하철_노선_변경_데이터를_만든다();
        return RestAssured.given().log().all()
                .accept(ContentType.ANY)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when()
                .put(uri) // 모든 데이터를 변경하고 있어서 put 으로 했습니다.
                .then().log().all()
                .extract();
    }

    private static Map<String, String> 지하철_노선_변경_데이터를_만든다() {
        final Map<String, String> params = new HashMap<>();
        final String updatedName = "구분당선";
        final String updatedColor = "bg-blue-600";
        params.put("name", updatedName);
        params.put("color", updatedColor);
        return params;
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제를_요청한다(final String uri) {
        return RestAssured.given().log().all()
                .accept(ContentType.ANY)
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_구간_등록을_요청한다(final String lineId, final String upStationId, final String downStationId, final int distance) {
        final Map<String, String> params = 지하철_노선_구간_데이터를_만든다(upStationId, downStationId, distance);
        return RestAssured.given().log().all()
                .body(params)
                .accept(ContentType.ANY)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(String.format("/lines/%s/sections", lineId))
                .then().log().all()
                .extract();
    }

    private static Map<String, String> 지하철_노선_구간_데이터를_만든다(final String upStationId, final String downStationId, final int distance) {
        final Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", String.valueOf(distance));
        return params;
    }
}
