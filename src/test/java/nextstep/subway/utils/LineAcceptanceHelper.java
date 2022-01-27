package nextstep.subway.utils;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class LineAcceptanceHelper {
    public static ExtractableResponse<Response> 노선_조회(final String createdLineId) {
        return CustomRestAssured.get("/lines/" + createdLineId);
    }

    public static ExtractableResponse<Response> 노선_생성(final String lineName, final String lineColor, String upStationId, String downStationId, String distance) {
        return CustomRestAssured.post("/lines/", lineCreateParams(lineName, lineColor, upStationId, downStationId, distance));
    }

    public static ExtractableResponse<Response> 노선_정보_변경(final String id, final String name, final String color) {
        return CustomRestAssured.put("/lines/" + id, lineUpdateParams(name, color));
    }

    private static Map<String, String> lineCreateParams(final String lineName, final String lineColor, String upStationId, String downStationId, String distance) {
        Map<String, String> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", lineColor);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        return params;
    }

    private static Map<String, String> lineUpdateParams(final String lineName, final String lineColor) {
        Map<String, String> params = new HashMap<>();
        params.put("name", lineName);
        params.put("color", lineColor);

        return params;
    }
}
