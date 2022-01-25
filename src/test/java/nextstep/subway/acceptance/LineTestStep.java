package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

public class LineTestStep {

    public static Long 지하철_노선_생성한_후_아이디_추출하기(String lineColor, String lineName) {
        ExtractableResponse<Response> response = 지하철_노선을_생성한다(lineColor, lineName);
        Integer responseIntegerId = response.jsonPath().get("id");
        return responseIntegerId.longValue();
    }

    public static ExtractableResponse<Response> 지하철_노선을_생성한다(String lineColor, String lineName) {
        Map<String, String> body = new HashMap<>();
        body.put("color", lineColor);
        body.put("name", lineName);

        return RestAssured
                .given().log().all()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_목록을_조회한다() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선을_조회한다(Long id) {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/lines/" + id)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선을_수정한다(Long lineId, String lineColor, String lineName) {
        Map<String, String> body = new HashMap<>();
        body.put("color", lineColor);
        body.put("name", lineName);

        return RestAssured
                .given().log().all()
                .body(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/lines/" + lineId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선을_삭제한다(Long lineId) {
        return RestAssured
                .given().log().all()
                .when()
                .delete("/lines/" + lineId)
                .then().log().all()
                .extract();
    }
}
