package nextstep.subway.utils;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class SectionAcceptanceHelper {

    public static ExtractableResponse<Response> 구간_추가(final String upStationId, final String downStationId, final String lineId, String distance) {
        Map<String, String> params = sectionAddParams(upStationId, downStationId, distance);

        return CustomRestAssured.post("/lines/" + lineId + "/sections", params);
    }

    public static ExtractableResponse<Response> 구간_제거(final String lineId, String stationId) {
        return CustomRestAssured.delete("/lines/" + lineId + "/sections?stationId=" + stationId);
    }

    private static Map<String, String> sectionAddParams(final String upStationId, final String downStationId, final String distance) {
        Map<String, String> params = new HashMap<>();
        params.put("distance", distance);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        return params;
    }
}
