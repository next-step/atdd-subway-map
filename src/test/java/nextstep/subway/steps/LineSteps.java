package nextstep.subway.steps;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.application.dto.LineRequest;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class LineSteps {
    public static ExtractableResponse<Response> executeLineListGetRequest() {
        return RestAssured.given()
                .when()
                .get("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> executeLineGetRequest(Long id) {
        return RestAssured.given()
                .when()
                .get("/lines/{id}", id)
                .then()
                .extract();
    }

    public static ExtractableResponse<Response> executeLineCreateRequest(String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        return RestAssured.given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then()
                .extract();
    }

    public static ExtractableResponse<Response> executeLineCreateRequest(LineRequest request) {
        return RestAssured.given()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then()
                .extract();
    }

    public static ExtractableResponse<Response> executeLineUpdateRequest(Long id, String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        return RestAssured.given()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/lines/{id}", id)
                .then()
                .extract();
    }

    public static ExtractableResponse<Response> executeLineDeleteRequest(Long id) {
        return RestAssured.given()
                .when()
                .delete("/lines/{id}", id)
                .then()
                .extract();
    }
}
