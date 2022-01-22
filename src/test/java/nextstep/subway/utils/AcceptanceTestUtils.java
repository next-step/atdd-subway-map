package nextstep.subway.utils;

import java.util.Map;
import java.util.Objects;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AcceptanceTestUtils {
    private AcceptanceTestUtils() {
    }

    public static ExtractableResponse<Response> requestLocation(ExtractableResponse<Response> response, Method method, Object body) {
        return given(body)
            .when()
            .request(method, getLocation(response))
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> requestLocation(ExtractableResponse<Response> response, Method method) {
        return requestLocation(response, method, null);
    }

    private static RequestSpecification given(Object body) {
        RequestSpecification given = RestAssured.given().log().all();
        if (Objects.isNull(body)) {
            return given;
        }
        return given.body(body)
                    .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    public static String getLocation(ExtractableResponse<Response> response) {
        return response.header("Location");
    }
}
