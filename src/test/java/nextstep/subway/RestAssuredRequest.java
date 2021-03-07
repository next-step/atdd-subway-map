package nextstep.subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class RestAssuredRequest {

    public static ExtractableResponse<Response> getRequest(String path) {
        return RestAssured.given().log().all()
                .when()
                .get(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> postRequest(String path, Map params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> putRequest(String path, Map params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteRequest(String path) {
        return RestAssured.given().log().all()
                .when()
                .delete(path)
                .then().log().all()
                .extract();
    }

}
