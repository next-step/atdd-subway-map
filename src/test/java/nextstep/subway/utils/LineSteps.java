package nextstep.subway.utils;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class LineSteps {

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String lineName, String lineColor) {
        LineRequest lineRequest = new LineRequest(lineName, lineColor);
        return RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 지하철_노선_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured.given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
    }

    public static void 지하철_노선_목록_조회됨(ExtractableResponse<Response> response, int inputCount) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(response.jsonPath().getList(".", LineResponse.class)).hasSize(inputCount);
    };

    public static void 지하철_노선_목록_포함됨(ExtractableResponse<Response> readAllLinesResponse,
                               List<ExtractableResponse<Response>> createLineResponses) {
        List<Long> resultLineIds = readAllLinesResponse.jsonPath()
                .getList(".", LineResponse.class)
                .stream()
                .map(r -> r.getId())
                .collect(Collectors.toList());

        List<Long> expectedLineIds = createLineResponses
                .stream()
                .map(r -> Long.parseLong(r.header("Location").split("/")[2]))
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsExactlyInAnyOrderElementsOf(expectedLineIds);
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(ExtractableResponse<Response> createdResponse) {
        String uri = createdResponse.header("Location");
        return RestAssured.given().log().all()
                .when()
                .get(uri)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(String lineName, String lineColor,
                                                       ExtractableResponse<Response> createdResponse) {
        LineRequest lineRequest = new LineRequest(lineName, lineColor);
        String uri = createdResponse.header("Location");
        return RestAssured.given().log().all()
                .body(lineRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(uri)
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_수정됨(ExtractableResponse<Response> response, String lineName, String lineColor) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        LineResponse lineResponse = response.jsonPath().getObject(".", LineResponse.class);
        assertThat(lineResponse.getName()).isEqualTo(lineName);
        assertThat(lineResponse.getColor()).isEqualTo(lineColor);
    }

    public static ExtractableResponse<Response> 지하철_노선_제거_요청(ExtractableResponse<Response> createdResponse) {
        String uri = createdResponse.header("Location");
        return RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }

    public static void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
