package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.StationSteps.지하철_역_생성_요청;

public class LineSteps {

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(
            final String name, final String color, final Long upStationId, final Long downStationId, final int distance
    ) {
        final Map<String, String> requestParams = new HashMap<>();
        requestParams.put("name", name);
        requestParams.put("color", color);
        requestParams.put("upStationId", upStationId.toString());
        requestParams.put("downStationId", downStationId.toString());
        requestParams.put("distance", Integer.toString(distance));

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestParams)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(
            final String name, final String color, final String upStationName, final String downStationName
    ) {
        final ExtractableResponse<Response> createStationResponse1 = 지하철_역_생성_요청(upStationName);
        final ExtractableResponse<Response> createStationResponse2 = 지하철_역_생성_요청(downStationName);
        return 지하철_노선_생성_요청(
                name,
                color,
                createStationResponse1.jsonPath().getLong("id"),
                createStationResponse2.jsonPath().getLong("id"),
                1
        );
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(final String name, final String color) {
        return 지하철_노선_생성_요청(name, color, "강남역", "역삼역");
    }

    public static ExtractableResponse<Response> 지하철_노선_생성_요청() {
        return 지하철_노선_생성_요청("신분당선", "bg-red-600");
    }

            public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return 지하철_노선_조회_요청("/lines");
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(final String path) {
        return RestAssured.given().log().all()
                .when()
                .get(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(final String path, final String name, final String color) {
        final Map<String, String> requestParams = new HashMap<>();
        requestParams.put("name", name);
        requestParams.put("color", color);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestParams)
                .when()
                .put(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제_요청(final String path) {
        return RestAssured.given().log().all()
                .when()
                .delete(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_구간_등록_요청(
            final Long lineId, final Long upStationId, final Long downStationId, final int distance
    ) {
        final Map<String, String> requestParams = new HashMap<>();
        requestParams.put("upStationId", upStationId.toString());
        requestParams.put("downStationId", downStationId.toString());
        requestParams.put("distance", Integer.toString(distance));

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestParams)
                .when()
                .post(String.format("/lines/%d/sections", lineId))
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_구간_제거_요청(final Long lineId, final Long downStationId) {
        return RestAssured.given().log().all()
                .queryParam("stationId", downStationId)
                .when()
                .delete("/lines/{lineId}/sections", lineId)
                .then().log().all()
                .extract();
    }
}
