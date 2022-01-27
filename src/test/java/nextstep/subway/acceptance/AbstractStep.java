package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public abstract class AbstractStep {
    private final String requestUrl;

    protected AbstractStep(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    protected ExtractableResponse<Response> request(RequestSpecification given, Method method, String url) {
        return given.when()
                    .request(method, url)
                    .then().log().all()
                    .extract();
    }

    protected ExtractableResponse<Response> request(Method method, String url) {
        return request(
            RestAssured.given().log().all(),
            method,
            url
        );
    }
}
