package nextstep.subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class AssuredRequest {

    public static ExtractableResponse<Response> doCreate(String endPoint, Map<String, String> map) {
        return RestAssured.given().log().all()
                .body(map)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(endPoint)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> doFindAll(String endPoint) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(endPoint)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> doFind(String uri) {
        return RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> doUpdate(String uri, Map<String, String> map) {
        return RestAssured.given().log().all()
                .body(map)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> doDelete(String uri) {
        return RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> doDelete(String uri, Map<String, String> queryMap) {
        return RestAssured.given().log().all()
                .when()
                .queryParams(queryMap)
                .delete(uri)
                .then().log().all()
                .extract();
    }
}
