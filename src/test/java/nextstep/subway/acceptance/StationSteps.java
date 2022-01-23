package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class StationSteps {
    public static String URL = "/stations";

    public static ExtractableResponse<Response> post(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return CommonRestAssured.post(URL, params);
    }

    public static ExtractableResponse<Response> delete(String url) {
        return CommonRestAssured.delete(url);
    }

    public static ExtractableResponse<Response> get() {
        return CommonRestAssured.get(URL);
    }
}
