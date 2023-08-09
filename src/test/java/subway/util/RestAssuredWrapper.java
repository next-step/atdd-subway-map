package subway.util;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.Map;

import static io.restassured.RestAssured.given;

@UtilityClass
public class RestAssuredWrapper {
    public static ExtractableResponse<Response> get(String path) {
        return request(path, null, null, HttpMethod.GET);
    }

    public static ExtractableResponse<Response> get(String path, Map<String, String> queryParams) {
        return request(path, null, queryParams, HttpMethod.GET);
    }

    public static ExtractableResponse<Response> post(String path, Object body) {
        return request(path, body, null, HttpMethod.POST);
    }

    public static ExtractableResponse<Response> put(String path, Object body) {
        return request(path, body, null, HttpMethod.PUT);
    }

    public static ExtractableResponse<Response> delete(String path) {
        return request(path, null, null, HttpMethod.DELETE);
    }

    private static ExtractableResponse<Response> request(String path, Object body, Map<String, String> queryParams, HttpMethod method) {
        RequestSpecification requestSpecification = given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        if (queryParams != null) {
            requestSpecification.queryParams(queryParams);
        }

        if (body != null) {
            requestSpecification.body(body);
        }

        return requestSpecification
                .when().request(method.name(), path)
                .then()
                .log().all()
                .extract();
    }
}
