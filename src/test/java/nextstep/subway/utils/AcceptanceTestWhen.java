package nextstep.subway.utils;

import java.util.Objects;

import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class AcceptanceTestWhen {
    private static final String LOCATION_KEY_IN_HEADER = "Location";

    private final ExtractableResponse<Response> response;

    private AcceptanceTestWhen(ExtractableResponse<Response> response) {
        this.response = response;
    }

    public static AcceptanceTestWhen fromGiven(ExtractableResponse<Response> response) {
        return new AcceptanceTestWhen(response);
    }

    public ExtractableResponse<Response> requestLocation(Method method, Object body) {
        return requestGiven(body)
            .when()
            .request(method, getLocation(response))
            .then().log().all()
            .extract();
    }

    public ExtractableResponse<Response> requestLocation(Method method) {
        return requestLocation(method, null);
    }

    private static RequestSpecification requestGiven(Object body) {
        RequestSpecification given = RestAssured.given().log().all();
        if (Objects.isNull(body)) {
            return given;
        }
        return given.body(body)
                    .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    public static String getLocation(ExtractableResponse<Response> response) {
        return response.header(LOCATION_KEY_IN_HEADER);
    }
}
