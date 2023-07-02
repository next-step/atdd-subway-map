package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class HttpRequest {
    public static ExtractableResponse<Response> sendPostRequest(String path, Map<String, String> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> sendDeleteRequest(String path) {
        return RestAssured.given().log().all()
                .when().delete(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> sendGetRequest(String path) {
        return RestAssured.given().log().all()
                .when().get(path)
                .then().log().all()
                .extract();
    }
}
