package nextstep.subway.line;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.RestAssuredRequest.*;
import static org.assertj.core.api.Assertions.assertThat;

public class LineSteps {

    public static ExtractableResponse<Response> 지하철_노선_생성_요청(String name, String color) {
        Map<String, String> params = createParams(name, color);
        return postRequest("/lines", params);
    }

    public static ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return getRequest("/lines");
    }

    public static void 지하철_노선_생성_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 지하철_노선_생성_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철_노선_목록_조회_결과_2건(ExtractableResponse<Response> response) {
        List<LineResponse> resultLines = response.jsonPath().getList(".", LineResponse.class);
        assertThat(resultLines).hasSize(2);
    }

    public static void 지하철_노선_조회_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 존재하지_않는_지하철_노선(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    public static ExtractableResponse<Response> 지하철_노선_조회_요청(Long createdLineId) {
        return getRequest("/lines/"+createdLineId);
    }

    public static Long 생성된_지하철_노선_ID_확인(ExtractableResponse<Response> createdLine) {
        return Long.parseLong(createdLine.header("Location").split("/")[2]);
    }


    public static void 지하철_노선_수정_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 지하철_노선_수정_요청(Long createdLineId, String name, String color) {
        Map<String, String> params = createParams(name,color);
        return putRequest("/lines/"+createdLineId, params);
    }

    public static void 지하철_노선_삭제_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static ExtractableResponse<Response> 지하철_노선_삭제_요청(Long createdLineId) {
        return deleteRequest("/lines/"+createdLineId);
    }


    private static Map<String, String> createParams(String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        return params;
    }
}
