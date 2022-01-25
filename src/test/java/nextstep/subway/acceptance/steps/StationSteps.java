package nextstep.subway.acceptance.steps;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class StationSteps {
    public static final String URL = "/stations";

    public static ExtractableResponse<Response> create(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return CommonRestAssured.create(URL, params);
    }

    public static ExtractableResponse<Response> delete(String url) {
        return CommonRestAssured.delete(url);
    }

    public static ExtractableResponse<Response> get() {
        return CommonRestAssured.get(URL);
    }
}
