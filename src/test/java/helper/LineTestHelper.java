package helper;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import subway.dto.LineRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LineTestHelper {
    public static ExtractableResponse<Response> 지하철노선을_생성한다(LineRequest lineRequest) {
        return RequestApiHelper.post("/lines", lineRequest);
    }

    public static List<String> 지하철노선_목록을_조회한다() {
        return RequestApiHelper.get("/lines")
                .jsonPath()
                .getList("id", String.class);
    }

    public static ExtractableResponse<Response> 지하철노선을_조회한다(String lineId) {
        return RequestApiHelper.get("/lines/" + lineId);
    }

    public static ExtractableResponse<Response> 지하철노선을_수정한다(String lineId, String name, String color) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        return RequestApiHelper.post("/lines/" + lineId, params);
    }

    public static ExtractableResponse<Response> 지하철노선을_삭제한다(String stationId) {
        return RequestApiHelper.delete("/lines/" + stationId);
    }
}
