package subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.utils.RestAssuredClient;

import java.util.Map;

public class LineRestAssuredClient {
    public static ExtractableResponse<Response> createLine(Map<String, Object> requestParam) {
        return RestAssuredClient.post("/lines", requestParam);
    }

    public static ExtractableResponse<Response> findLine(Long id) {
        return RestAssuredClient.get(String.format("/lines/%d", id));
    }

    public static ExtractableResponse<Response> updateLine(Long id, Map<String, Object> requestParam) {
        return RestAssuredClient.put(String.format("/lines/%d", id), requestParam);
    }

    public static ExtractableResponse<Response> listLine() {
        return RestAssuredClient.get("/lines");
    }

    public static ExtractableResponse<Response> deleteLine(Long id) {
        return RestAssuredClient.delete(String.format("/lines/%d", id));
    }
}
