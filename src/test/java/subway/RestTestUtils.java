package subway;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

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

    public static ExtractableResponse<Response> request(Method httpMethod, String requestUrl) {
        return RestAssured.given().log().all()
                .when().request(httpMethod,requestUrl)
                .then().log().all()
                .extract();
    }

    public static <T> ExtractableResponse<Response> request(Method httpMethod, String requestUrl, T body) {
        return RestAssured.given().log().all()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().request(httpMethod, requestUrl)
                .then().log().all()
                .extract();
    }

}
