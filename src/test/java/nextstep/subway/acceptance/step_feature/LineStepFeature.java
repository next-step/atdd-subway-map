package nextstep.subway.acceptance.step_feature;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.MediaType;

public class LineStepFeature {

    public static final String SHINBUNDANG_LINE_NAME = "신분당선";
    public static final String NUMBER2_LINE_NAME = "2호선";
    private static final String CREATE_LINE_NAME_PARAM_KEY = "name";
    private static final String CREATE_LINE_COLOR_PARAM_KEY = "color";
    private static final String LINE_BASE_URI = "lines";

    public static ExtractableResponse<Response> callCreateLines(Map<String, String> lineParams) {
        return RestAssured.given()
            .log()
            .all()
            .body(lineParams)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post(LINE_BASE_URI)
            .then()
            .log()
            .all()
            .extract();
    }

    public static ExtractableResponse<Response> callGetLines() {
        return RestAssured.given()
            .log()
            .all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get(LINE_BASE_URI)
            .then()
            .log()
            .all()
            .extract();
    }

    public static ExtractableResponse<Response> callGetLines(long id) {
        return RestAssured.given()
            .log()
            .all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get(LINE_BASE_URI + "/" + id)
            .then()
            .log()
            .all()
            .extract();
    }

    public static ExtractableResponse<Response> callUpdateLines(Map<String, String> lineParams) {
        return RestAssured.given()
            .log()
            .all()
            .body(lineParams)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .put(LINE_BASE_URI + "/" + lineParams.get("id"))
            .then()
            .log()
            .all()
            .extract();
    }

    public static ExtractableResponse<Response> callDeleteLines(long id) {
        return RestAssured.given()
            .log()
            .all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .delete(LINE_BASE_URI + "/" + id)
            .then()
            .log()
            .all()
            .extract();
    }

    public static Map<String, String> createShinbundangLineParams() {
        return createLineParams(SHINBUNDANG_LINE_NAME, "red");
    }

    public static Map<String, String> createNumber2LineParams() {
        return createLineParams(NUMBER2_LINE_NAME, "green");
    }

    private static Map<String, String> createLineParams(String name, String color) {
        Map<String, String> result = new HashMap();
        result.put(CREATE_LINE_NAME_PARAM_KEY, name);
        result.put(CREATE_LINE_COLOR_PARAM_KEY, color);

        return result;
    }

}
