package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

public class LineRestAssured {

    private static final String apiPath = "/lines";

    public static ExtractableResponse<Response> createRoute(LineRequest lineRequest) {
        return RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(apiPath)
                .then().log().all()
                .extract();
    }
    public static ExtractableResponse<Response> readLines() {
        return RestAssured.given().log().all()
                .when().get(apiPath)
                .then().log().all()
                .extract();
    }
}
