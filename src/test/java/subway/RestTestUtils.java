package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.List;

public class RestTestUtils {

    public static <T> List<T> getListFromResponse(ExtractableResponse<Response> response, String key, Class<T> type) {
        return response.jsonPath().getList(key, type);
    }

    public static Long getLongFromResponse(ExtractableResponse<Response> response, String key) {
        return response.jsonPath().getLong(key);
    }

    public static String getStringFromResponse(ExtractableResponse<Response> response, String key) {
        return response.jsonPath().getString(key);
    }

}
