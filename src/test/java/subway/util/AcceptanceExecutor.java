package subway.util;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class AcceptanceExecutor {

    public static <T> ExtractableResponse<Response> post(final String path, final T requestBody) {

        return RestAssured
                .given().body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(path)
                .then().log().all()
                .extract();
    }

    public static <R> R get(final String url, final Class<R> responseType) {

        return RestAssured
                .when().get(url)
                .then().log().all()
                .extract()
                .as(responseType);
    }

    public static ExtractableResponse<Response> get(final String path) {

        return RestAssured
                .when().get(path)
                .then().log().all()
                .extract();
    }

    public static <T> void put(final String path, final T requestBody) {

        RestAssured.given().log().all()
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(path)
                .then().log().all()
                .extract();
    }

    public static <T> ExtractableResponse<Response> delete(final String path) {

        return RestAssured
                .when().delete(path)
                .then().log().all()
                .extract();
    }

}
