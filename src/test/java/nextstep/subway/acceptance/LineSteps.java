package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.Map;

public class LineSteps {

    private static final String LINES_URI = "/lines";

    public static ExtractableResponse<Response> createLine(Map<String, String> param) {
        return RestAssured.given().log().all()
                .body(param)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(LINES_URI)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> findLines() {
        return RestAssured.given().log().all()
                .when()
                .get(LINES_URI)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> findLines(String uri) {
        return RestAssured.given().log().all()
                .when()
                .get(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> updateLine(String uri, Map<String, String> line) {
        return RestAssured.given().log().all()
                .body(line)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> deleteLine(String uri) {
        return RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }
}
