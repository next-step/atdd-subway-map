package nextstep.subway.acceptance.steps;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class LineSteps {
    private static final String URL = "/lines";

    public static ExtractableResponse<Response> create(String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        return CommonRestAssured.create(URL, params);
    }

    public static ExtractableResponse<Response> get() {
        return CommonRestAssured.get(URL);
    }

    public static ExtractableResponse<Response> get(Long lineId) {
        return CommonRestAssured.get(URL + "/" + lineId);
    }

    public static ExtractableResponse<Response> delete(String url) {
        return CommonRestAssured.delete(url);
    }

    public static ExtractableResponse<Response> modify(String url, String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        return CommonRestAssured.modify(url, params);
    }
}
