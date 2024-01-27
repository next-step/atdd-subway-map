package subway.util;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.List;

public class JsonPathUtil {

    public static Long getId(ExtractableResponse<Response> response) {
        return response.jsonPath().getLong("id");
    }

    public static List<Long> getIds(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("id", Long.class);
    }

    public static List<String> getNames(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("name", String.class);
    }
}
