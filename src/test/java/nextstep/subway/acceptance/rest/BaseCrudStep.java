package nextstep.subway.acceptance.rest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import java.util.Map;

public class BaseCrudStep {

    public static ExtractableResponse<Response> createResponse(
            String uri,
            Map<String, String> params
    ) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(ContentType.JSON)
                .when()
                .post(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> readResponse(String uri) {
        return RestAssured.given().log().all()
                .accept(ContentType.JSON)
                .when()
                .get(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> updateResponse(
            String uri,
            Map<String, String> params
    ) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when()
                .put(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteResponse(String uri) {
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .delete(uri)
                .then().log().all()
                .extract();
    }
}
