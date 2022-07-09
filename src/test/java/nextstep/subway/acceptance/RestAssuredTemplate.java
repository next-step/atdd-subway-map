package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class RestAssuredTemplate {

    public static ExtractableResponse<Response> getRequest(String url) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(url)
                .then().log().all()
                .extract();
    }

    public static <T> ExtractableResponse<Response> getRequestWithParameter(String url, T parameter) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(url, parameter)
                .then().log().all()
                .extract();
    }

    public static <T> ExtractableResponse<Response> postRequestWithRequestBody(String url, T body) {
        return RestAssured.given().log().all()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(url)
                .then().log().all()
                .extract();
    }

    public static <T> ExtractableResponse<Response> deleteRequestWithParameter(String url, T parameter) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(url, parameter)
                .then().log().all()
                .extract();
    }

    public static <T> ExtractableResponse<Response> putRequestWithParameterAndRequestBody(String url, T parameter, T body) {
        return RestAssured.given().log().all()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(url, parameter)
                .then().log().all()
                .extract();
    }
}
