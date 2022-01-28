package nextstep.subway.utils;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;

import java.net.URI;
import java.util.Map;

public class RestTestUtils {

    public static ExtractableResponse<Response> 요청_테스트(URI uri, Method httpMethod) {
        return createResponseExtractableResponse(uri, httpMethod, createRequestSpecificationWithLog());
    }

    public static ExtractableResponse<Response> 요청_테스트(URI uri, Map<String, ?> params, Method httpMethod) {
        RequestSpecification requestSpecification = createRequestSpecificationWithLog()
                .body(params);

        return createResponseExtractableResponse(uri, httpMethod, requestSpecification);
    }

    private static RequestSpecification createRequestSpecificationWithLog() {
        return RestAssured.given().log().all();
    }

    private static ExtractableResponse<Response> createResponseExtractableResponse(URI uri, Method httpMethod, RequestSpecification requestSpecification) {
        return requestSpecification
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .request(httpMethod, uri)
                .then().log().all()
                .extract();
    }

    public static URI getLocationURI(ExtractableResponse<Response> response) {
        return URI.create(response.header("Location"));
    }

    public static Long getCreatedResourceId(ExtractableResponse<Response> response) {
        return response.jsonPath().getLong("id");
    }
}
