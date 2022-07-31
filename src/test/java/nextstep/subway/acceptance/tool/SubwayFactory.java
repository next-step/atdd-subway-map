package nextstep.subway.acceptance.tool;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.line.dto.LineRequest;
import nextstep.subway.applicaion.section.dto.SectionRequest;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.acceptance.tool.RequestTool.post;

public class SubwayFactory {

    public static ExtractableResponse<Response> 역_생성(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return post("/stations", params);
    }

    public static ExtractableResponse<Response> 노선_생성(String name, String color, Long upStationId, Long downStationId, Integer distance) {
        LineRequest lineRequest = new LineRequest(name, color, upStationId, downStationId, distance);
        return post("/lines", lineRequest);
    }

    public static ExtractableResponse<Response> 구간_생성(Long lineId, Long upStationId, Long downStationId, Integer distance) {
        SectionRequest sectionRequest = new SectionRequest(upStationId, downStationId, distance);
        return post("/lines/" + lineId + "/sections", sectionRequest);
    }
}
