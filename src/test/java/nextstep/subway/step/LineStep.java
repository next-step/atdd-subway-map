package nextstep.subway.step;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.LineRequest;
import org.springframework.http.MediaType;

public class LineStep {

    private static final String DEFAULT_PATH = "/lines";

    public static ExtractableResponse<Response> 노선_생성(String name, String color, Long upStation, Long downStation, int distance) {
        LineRequest request = new LineRequest(name, color, upStation, downStation, distance);

        return RestAssured.given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post(DEFAULT_PATH)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 노선_목록_조회(String path) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .get(path)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 노선_변경(String path, Object requestBody) {
        return RestAssured.given().log().all()
            .body(requestBody)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .put(path)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 노선_삭제(String path) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .delete(path)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 구간_생성(String path, Object request) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(request)
            .when()
            .post(path)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 구간_삭제(String path, String param, Long value) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .param(param, value)
            .when()
            .delete(path)
            .then().log().all()
            .extract();
    }

}
