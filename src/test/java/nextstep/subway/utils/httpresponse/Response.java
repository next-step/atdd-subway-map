package nextstep.subway.utils.httpresponse;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import org.springframework.http.MediaType;

import java.util.Map;

public class Response {

    public static ExtractableResponse<io.restassured.response.Response> post(Map<String, String> requestDto, String uri) {
        return RestAssured.given().log().all()
                .body(requestDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<io.restassured.response.Response> get(String uri) {
        return RestAssured.given().log().all()
                .when()
                .get(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<io.restassured.response.Response> put(Map<String, String> requestDto, String uri) {
        return RestAssured.given().log().all()
                .body(requestDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<io.restassured.response.Response> delete(String uri) {
        return RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }
}
