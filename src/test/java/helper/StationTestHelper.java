package helper;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StationTestHelper {
    public static ExtractableResponse<Response> 지하철역을_생성한다(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return RequestApiHelper.post("/stations", params);
    }

    public static ExtractableResponse<Response> 지하철역을_삭제한다(String stationId) {
        return RequestApiHelper.delete("/stations/" + stationId);
    }

    public static List<String> 지하철역_목록을_조회한다() {
        return RequestApiHelper.get("/stations")
                .jsonPath()
                .getList("name", String.class);
    }
}
