package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class LineSteps {
    private static String URL = "/lines";

    public static ExtractableResponse<Response> post(String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        return CommonRestAssured.post(URL, params);
    }

    public static ExtractableResponse<Response> get() {
        return CommonRestAssured.get(URL);
    }

    public static ExtractableResponse<Response> delete(String url) {
        return CommonRestAssured.delete(url);
    }

    public static ExtractableResponse<Response> put(String url, String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        return CommonRestAssured.put(url, params);
    }
}
