package subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;
import subway.line.presentation.LineRequest;

public class LineRestAssured {

    private static final String API_PATH = "/lines";

    public static ExtractableResponse<Response> createLine(LineRequest lineRequest) {
        return RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(API_PATH)
                .then().log().all()
                .extract();
    }
    public static ExtractableResponse<Response> readLines() {
        return RestAssured.given().log().all()
                .when().get(API_PATH)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> readLine(Long id) {
        return RestAssured.given().log().all()
                .when().get(API_PATH + "/" + id)
                .then().log().all()
                .extract();
    }
}
