package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class LineSteps {

    public static ExtractableResponse<Response> 지하철_노선_생성요청(Map<String, String> params) {
        return RestAssured
                .given().log().all().body(params).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines")
                .then().log().all().extract();
    }

    public static void 지하철_노선_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 지하철_노선_생성실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static ExtractableResponse<Response> 지하철_노선목록_조회요청() {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().get("/lines")
                .then().log().all().extract();
        return response;
    }

    public static void 지하철_노선목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> resultLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());

        List<Long> expectedLineIds = createdResponses.stream()
                .map(it -> it.as(LineResponse.class))
                .map(LineResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    public static ExtractableResponse<Response> 지하철_노선_조회요청(Long lineId) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when().get("/lines/{id}", lineId)
                .then().log().all().extract();
        return response;
    }

    public static void 지하철_노선_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_포함됨(ExtractableResponse<Response> response, ExtractableResponse<Response> createdResponse) {
        Long resultLineId = response.as(LineResponse.class).getId();
        Long expectedLineId = createdResponse.as(LineResponse.class).getId();

        assertThat(resultLineId).isEqualTo(expectedLineId);
    }

    public static ExtractableResponse<Response> 지하철_노선_수정요청(Long lineId, Map<String, String> params) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all().body(params).contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/lines/{id}", lineId)
                .then().log().all().extract();

        return response;
    }

    public static void 지하철_노선_수정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 지하철_노선_확인됨(ExtractableResponse<Response> response, String name, String color) {
        String updatedName = response.as(LineResponse.class).getName();
        String updatedColor = response.as(LineResponse.class).getColor();

        assertThat(updatedName).isEqualTo(name);
        assertThat(updatedColor).isEqualTo(color);
    }

    public static ExtractableResponse<Response> 지하철_노선_제거요청(Long lineId) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/lines/{id}", lineId)
                .then().log().all().extract();

        return response;
    }

    public static void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public enum Line {

        일호선("일호선", "노랑색"),
        분당선("분당선", "빨강색"),
        에버라인("에버라인", "파랑색");

        public String name;
        public String color;

        Line(String name, String color) {
            this.name = name;
            this.color = color;
        }
    }

}
