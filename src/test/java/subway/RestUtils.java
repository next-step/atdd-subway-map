package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.List;

public class RestUtils {

    public static <T> List<T> getListFromResponse(ExtractableResponse<Response> response, String key, Class<T> type) {
        return response.jsonPath().getList(key, type);
    }

}
