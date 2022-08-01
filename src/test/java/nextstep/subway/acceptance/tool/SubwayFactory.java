package nextstep.subway.acceptance.tool;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.line.dto.LineRequest;

import java.util.HashMap;
import java.util.Map;

public class SubwayFactory {

    public static ExtractableResponse<Response> 역_생성(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return RequestTool.post("/stations", params);
    }

    public static ExtractableResponse<Response> 노선_생성(String name, String color, Long upStationId, Long downStationId, Integer distance) {
        LineRequest lineRequest = new LineRequest(name, color, upStationId, downStationId, distance);
        return RequestTool.post("/lines", lineRequest);
    }
}
