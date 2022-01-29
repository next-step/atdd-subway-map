package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class LineStepDefinition {

    public static Map<String, String> 지하철_노선_파라미터_생성(
            String name,
            String color,
            Long upStationId,
            Long downStationId,
            int distance) {

        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", String.valueOf(upStationId));
        params.put("downStationId", String.valueOf(downStationId));
        params.put("distance", String.valueOf(distance));
        return params;
    }


    public static ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, String> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(String uri) {
        return RestAssured.given().log().all()
                .when()
                .get(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(String uri, Map<String, String> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제_요청(String uri) {
        return RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_응답_상태_검증(ExtractableResponse<Response> response, HttpStatus status) {
        assertThat(response.statusCode()).isEqualTo(status.value());
    }

    public static void 지하철_노선_생성_완료(ExtractableResponse<Response> response) {
        지하철_노선_응답_상태_검증(response, HttpStatus.CREATED);
    }

    public static void 지하철_노선_목록_조회_완료(ExtractableResponse<Response> response, String...lines) {
        지하철_노선_응답_상태_검증(response, HttpStatus.OK);
        assertThat(response.jsonPath().getList("name")).containsExactlyInAnyOrder(lines);
    }

    public static void 지하철_노선_조회_완료(ExtractableResponse<Response> response, String line) {
        지하철_노선_응답_상태_검증(response, HttpStatus.OK);
        assertThat(response.jsonPath().getString("name")).isEqualTo(line);
    }

    public static void 지하철_노선_삭제_완료(ExtractableResponse<Response> response) {
        지하철_노선_응답_상태_검증(response, HttpStatus.NO_CONTENT);
    }

    public static void 지하철_노선_수정_완료(ExtractableResponse<Response> response, Map<String, String> params) {
        지하철_노선_응답_상태_검증(response, HttpStatus.OK);
        assertThat(response.jsonPath().getString("name")).isEqualTo(params.get("name"));
        assertThat(response.jsonPath().getString("color")).isEqualTo(params.get("color"));
    }

    public static void 지하철_노선_생성_실패(ExtractableResponse<Response> response) {
        지하철_노선_응답_상태_검증(response, HttpStatus.BAD_REQUEST);
    }

}
