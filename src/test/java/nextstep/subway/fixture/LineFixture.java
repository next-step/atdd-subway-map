package nextstep.subway.fixture;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static nextstep.subway.utils.httpresponse.Response.get;
import static nextstep.subway.utils.httpresponse.Response.post;

public class LineFixture {
    public static final String 신분당선_이름 = "신분당선";
    public static final String 신분당선_색상 = "bg-red-600";

    public static final String 구분당선_이름 = "구분당선";
    public static final String 구분당선_색상 = "bg-blue-600";

    public static ExtractableResponse<Response> 지하철_노선_조회(String uri) {
        return get(uri);
    }

    public static ExtractableResponse<Response> 지하철_노선_생성(String name, String color, Long upStationId, Long downStationId, int distance) {
        Map<String, String> line = 노선(name, color, upStationId, downStationId, distance);
        return post(line, "/lines");
    }

    public static Map<String, String> 노선(String name, String color, Long upStationId, Long downStationId, int distance) {
        Map<String, String> line = new HashMap<>();
        line.put("name", name);
        line.put("color", color);
        line.put("upStationId", Long.toString(upStationId));
        line.put("downStationId", Long.toString(downStationId));
        line.put("distance", Integer.toString(distance));
        return line;
    }
}
