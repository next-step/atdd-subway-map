package nextstep.subway.acceptance.steps;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class SectionSteps {
    private static final String SECTION_URL = "/sections";

    public static ExtractableResponse<Response> create(String resource, Long upStationId, Long downStationId, int distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        return CommonRestAssured.create(getSectionUrl(resource), params);
    }

    public static ExtractableResponse<Response> delete(String resource, String stationId) {
        String url = getSectionUrl(resource) + "?stationId=" + stationId;
        return CommonRestAssured.delete(url);
    }

    public static ExtractableResponse<Response> modify(String url, String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        return CommonRestAssured.modify(url, params);
    }

    private static String getSectionUrl(String resource) {
        return resource + SECTION_URL;
    }
}
