package nextstep.subway.station;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.RestAssuredRequest.*;
import static org.assertj.core.api.Assertions.assertThat;

public class StationSteps {

    public static ExtractableResponse<Response> 강남역_생성() {
        return 지하철_역_생성_요청("강남역");
    }

    public static ExtractableResponse<Response> 역삼역_생성() {
        return 지하철_역_생성_요청("역역");
    }

    public static ExtractableResponse<Response> 지하철_역_생성_요청(String name) {
        Map<String, String> params = createParams(name);
        return postRequest("/stations", params);
    }

    public static ExtractableResponse<Response> 지하철_역_목록_조회_요청() {
        return getRequest("/stations");
    }

    public static void 지하철_역_생성_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 생성된_지하철_역_uri_반환(ExtractableResponse<Response> response) {
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 지하철_역_생성_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 존재하지_않는_지하철_역이기_때문에_잘못된_요청(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 지하철_역_목록_조회_결과_2건(ExtractableResponse<Response> response) {
        List<LineResponse> resultLines = response.jsonPath().getList(".", LineResponse.class);
        assertThat(resultLines).hasSize(2);
    }

    public static void 지하철_역_조회_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 지하철_역_조회_요청(Long createdLineId) {
        return getRequest("/stations/"+createdLineId);
    }

    public static Long 생성된_지하철_역_ID_확인(ExtractableResponse<Response> createdLine) {
        return Long.parseLong(createdLine.header("Location").split("/")[2]);
    }


    public static void 지하철_역_수정_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static ExtractableResponse<Response> 지하철_역_수정_요청(Long createdLineId, String name) {
        Map<String, String> params = createParams(name);
        return putRequest("/stations/"+createdLineId, params);
    }

    public static void 지하철_역_삭제_성공(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static ExtractableResponse<Response> 지하철_역_삭제_요청(Long createdLineId) {
        return deleteRequest("/stations/"+createdLineId);
    }


    private static Map<String, String> createParams(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return params;
    }

}
