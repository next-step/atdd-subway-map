package subway.util;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

public class Extractor
{
    public static ExtractableResponse<Response> get(String path) {

        return then(when().get(path));
    }

    public static ExtractableResponse<Response> post(String path, Object param) {

        return then(when(param).post(path));
    }

    public static ExtractableResponse<Response> put(String path, Object param) {

        return then(when(param).put(path));
    }

    public static ExtractableResponse<Response> delete(String path) {

        return then(when().delete(path));
    }

    private static RequestSpecification restAssured() {

        return RestAssured.given().log().all();
    }

    private static RequestSpecification when() {

        return restAssured().when();
    }

    private static RequestSpecification when(Object param) {

        return restAssured().when()
            .body(param)
            .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    private static ExtractableResponse<Response> then(Response response) {

        return response.then().extract();
    }

    public static Long getId(ExtractableResponse<Response> response) {

        return response.jsonPath().getLong("id");
    }
}
