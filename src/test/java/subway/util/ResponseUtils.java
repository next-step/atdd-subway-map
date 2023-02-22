package subway.util;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class ResponseUtils {

    public static int 응답코드(ExtractableResponse<Response> response) {
        return response.statusCode();
    }

    public static JsonPath Json_추출(ExtractableResponse<Response> response) {
        return response.jsonPath();
    }
}
