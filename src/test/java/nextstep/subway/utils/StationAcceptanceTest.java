package nextstep.subway.utils;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class StationAcceptanceTest {
    public static ExtractableResponse<Response> 지하철역_조회() {
        return CustomRestAssured.get("/stations");
    }

    public static ExtractableResponse<Response> 지하철역_생성(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return CustomRestAssured.post("/stations", params);
    }
}
