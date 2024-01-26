package core;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class RestAssuredHelper {

    private final String pathPrefix;

    public RestAssuredHelper(final String pathPrefix) {
        this.pathPrefix = pathPrefix;
    }

    public ExtractableResponse<Response> get() {
        return RestAssured
                .given()
                .when().get(pathPrefix)
                .then().extract();
    }

    public ExtractableResponse<Response> post(final Object body) {
        return RestAssured
                .given()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(pathPrefix)
                .then().extract();
    }

    public ExtractableResponse<Response> deleteById(final Long id) {
        return RestAssured
                .given().pathParam("id", id)
                .when().delete(pathPrefix + "/{id}")
                .then().extract();
    }
}
