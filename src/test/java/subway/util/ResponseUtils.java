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

    public static Long Json_ID_추출(ExtractableResponse<Response> response) {
        return Json_추출(response).getLong("id");
    }

    public static String Json_이름_추출(ExtractableResponse<Response> response) {
        return Json_추출(response).getString("name");
    }

    public static String Json_색_추출(ExtractableResponse<Response> response) {
        return Json_추출(response).getString("color");
    }
}
