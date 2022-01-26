package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

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

    public static void 지하철_노선_생성_성공_검증하기(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
        Integer responseIntegerId = response.jsonPath().get("id");
        Long createdId = responseIntegerId.longValue();
        assertThat(createdId).isGreaterThan(0L);
    }

    public static void 지하철_노선_중복이름_생성_실패_검증하기(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
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

    public static void 지하철_노선_목록_조회_시_두_노선이_있는지_검증하기(ExtractableResponse<Response> response,
                                                     List<String> colors, List<String> names) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> lineColors = response.jsonPath().getList("color");
        assertThat(lineColors).containsExactly(colors.get(0), colors.get(1));
        List<String> lineNames = response.jsonPath().getList("name");
        assertThat(lineNames).containsExactly(names.get(0), names.get(1));
        List<List<Object>> lineStationsList = response.jsonPath().getList("stations");
        assertThat(lineStationsList).hasSizeGreaterThan(0);
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

    public static void 지하철_노선_조회_성공_검증하기(ExtractableResponse<Response> response,
                                         Long id, String color, String name) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        Integer receivedId = response.jsonPath().get("id");
        assertThat(receivedId.longValue()).isEqualTo(id);
        String receivedColor = response.jsonPath().get("color");
        assertThat(receivedColor).isEqualTo(color);
        String receivedName = response.jsonPath().get("name");
        assertThat(receivedName).isEqualTo(name);
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

    public static void 지하철_노선_수정_성공_검증하기(ExtractableResponse<Response> response,
                                         Long createdId, String updatedColor, String updatedName) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        ExtractableResponse<Response> updatedResponse = 지하철_노선을_조회한다(createdId);
        String receivedColor = updatedResponse.jsonPath().get("color");
        assertThat(receivedColor).isEqualTo(updatedColor);
        String receivedName = updatedResponse.jsonPath().get("name");
        assertThat(receivedName).isEqualTo(updatedName);
    }

    public static ExtractableResponse<Response> 지하철_노선을_삭제한다(Long lineId) {
        return RestAssured
                .given().log().all()
                .when()
                .delete("/lines/" + lineId)
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_삭제_성공_검증하기(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        ExtractableResponse<Response> linesResponse = 지하철_노선_목록을_조회한다();
        List<Object> lines = linesResponse.jsonPath().getList("$");
        assertThat(lines).hasSize(0);
    }
}
