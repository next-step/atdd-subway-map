package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;

public class AcceptanceTestHelper {
    static <T> ExtractableResponse<Response> post(String url, T request) {
        return given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(url)
                .then().log().all()
                .extract();
    }

    static <T> ExtractableResponse<Response> post(String url, T pathParams, T request) {
        return given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(url, pathParams)
                .then().log().all()
                .extract();
    }

    static ExtractableResponse<Response> get(String url) {
        return given().log().all()
                .when().get(url)
                .then().log().all()
                .extract();
    }

    static <T> ExtractableResponse<Response> get(String url, T pathParams) {
        return given().log().all()
                .when().get(url, pathParams)
                .then().log().all()
                .extract();
    }


    static <T> ExtractableResponse<Response> put(String url, T pathParams, T request) {
        return given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put(url, pathParams)
                .then().log().all()
                .extract();
    }

    static <T> ExtractableResponse<Response> delete(String url, T pathParams) {
        return given().log().all()
                .when().delete(url, pathParams)
                .then().log().all()
                .extract();
    }

    static <T> ExtractableResponse<Response> delete(String url, T pathParams, String queryParamName, T queryParams) {
        return given().log().all()
                .queryParams(queryParamName, queryParams)
                .when().delete(url, pathParams)
                .then().log().all()
                .extract();
    }
}
