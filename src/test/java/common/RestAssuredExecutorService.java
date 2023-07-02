package common;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class RestAssuredExecutorService {

    public static ExtractableResponse<Response> getForResponse(String path) {
        return RestAssured
                .given().log().all()
                .when()
                .get(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> postForResponseByBody(String path, Object parameter) {
        return RestAssured
                .given().log().all()
                .body(parameter)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteForResponse(String path, Map<String, Object> params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete(path, params)
                .then().log().all()
                .extract();
    }
}
