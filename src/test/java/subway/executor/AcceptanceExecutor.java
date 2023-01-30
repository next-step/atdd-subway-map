package subway.executor;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class AcceptanceExecutor {

    public static <T> ExtractableResponse<Response> post(final String url, final T requestBody) {
        return RestAssured
                .given().body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(url)
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

    public static <T, R> R get(final String url, final Class<T> requestBody, final Class<R> responseType) {

        return RestAssured
                .given()
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(url)
                .then().log().all()
                .extract()
                .as(responseType);
    }

    public static <T> ExtractableResponse<Response> put(final String path, final T requestBody) {

        return RestAssured.given().log().all()
                .body(requestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(path)
                .then().log().all()
                .extract();
    }

    public static <T> ExtractableResponse<Response> delete(final String url) {

        return RestAssured
                .when().delete(url)
                .then().log().all()
                .extract();
    }

}
