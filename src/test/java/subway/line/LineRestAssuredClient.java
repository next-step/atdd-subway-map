package subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.RestAssuredClient;

import java.util.Map;

public class LineRestAssuredClient {
    public static ExtractableResponse<Response> createLine(Map<String, Object> requestParam) {
        return RestAssuredClient.post("/lines", requestParam);
    }

    public static ExtractableResponse<Response> findLine(Long lineId) {
        return RestAssuredClient.get(String.format("/lines/%d", lineId));
    }

    public static ExtractableResponse<Response> updateLine(Long lineId, Map<String, Object> requestParam) {
        return RestAssuredClient.put(String.format("/lines/%d", lineId), requestParam);
    }

    public static ExtractableResponse<Response> listLine() {
        return RestAssuredClient.get("/lines");
    }

    public static ExtractableResponse<Response> deleteLine(Long lineId) {
        return RestAssuredClient.delete(String.format("/lines/%d", lineId));
    }

    public static ExtractableResponse<Response> addLineSection(Long lineId, Map<String, Object> requestParam) {
        return RestAssuredClient.post(String.format("/lines/%d/sections", lineId), requestParam);
    }

    public static ExtractableResponse<Response> deleteLineSection(Long lineId, Long stationId) {
        return RestAssuredClient.delete(String.format("/lines/%d/sections?stationId=%d", lineId, stationId));
    }
}
