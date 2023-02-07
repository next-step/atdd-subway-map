package subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class RestAssuredClient {
    public static ExtractableResponse<Response> post(
            String path,
            Object request
    ) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> get(
            String path
    ) {
        return RestAssured.given().log().all()
                .when()
                .get(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> delete(
            String path
    ) {
        return RestAssured.given().log().all()
                .when()
                .delete(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> delete(
            String path,
            long stationId
    ) {
        Map<String, String> params = new HashMap<>();
        params.put("stationId", String.valueOf(stationId));

        return RestAssured.given().log().all()
                .params(params)
                .when()
                .delete(path)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> put(
            String path,
            Object request
    ) {
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(path)
                .then().log().all()
                .extract();
    }
}
