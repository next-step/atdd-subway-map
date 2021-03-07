package nextstep.subway.line;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.line.dto.LineResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private static final String LINE_DX_NAME = "신분당선";
    private static final String LINE_DX_COLOR = "bg-red-600";
    private static final String LINE_TWO_NAME = "2호선";
    private static final String LINE_TWO_COLOR = "bg-green-600";
    private static final String LINE_BUNDANG_NAME = "구분당선";
    private static final String LINE_BUNDANG_COLOR = "bg-blue-600";
    private static final String HTTP_HEADER_LOCATION = "Location";
    private static final String PARAM_NAME = "name";
    private static final String PARAM_COLOR = "color";
    private static final String BASE_URL = "/lines";

    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        //given
        Map<String, String> 신분당선 = 노선_파라미터_설정(LINE_DX_NAME, LINE_DX_COLOR);

        // when
        // 지하철_노선_생성_요청
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(신분당선);

        // then
        // 지하철_노선_생성됨
        응답_상태코드_201(response);
        응답_헤더_로케이션_값_있음(response);
    }

    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void getLines() {
        // given
        // 지하철_노선_등록되어_있음
        Map<String, String> 신분당선 = 노선_파라미터_설정(LINE_DX_NAME, LINE_DX_COLOR);
        ExtractableResponse<Response> 신분당선응답 = 지하철_노선_생성_요청(신분당선);

        // 지하철_노선_등록되어_있음
        Map<String, String> 이호선 = 노선_파라미터_설정(LINE_TWO_NAME, LINE_TWO_COLOR);
        ExtractableResponse<Response> 이호선응답 = 지하철_노선_생성_요청(이호선);

        // when
        ExtractableResponse<Response> 노선목록조회응답 = 지하철_노선_목록_조회_요청();

        // then
        응답_상태코드_200(노선목록조회응답);
        지하철_노선_목록_포함됨(노선목록조회응답, 신분당선응답, 이호선응답);
    }

    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void getLine() {
        // given
        // 지하철_노선_등록되어_있음
        Map<String, String> 신분당선 = 노선_파라미터_설정(LINE_DX_NAME, LINE_DX_COLOR);
        ExtractableResponse<Response> 신분당선응답 = 지하철_노선_생성_요청(신분당선);

        // when
        ExtractableResponse<Response> 노선조회응답 = 지하철_노선_조회_요청(신분당선응답);

        // then
        // 지하철_노선_응답됨
        응답_상태코드_200(노선조회응답);
    }

    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void updateLine() {
        // given
        // 지하철_노선_등록되어_있음
        Map<String, String> 신분당선 = 노선_파라미터_설정(LINE_DX_NAME, LINE_DX_COLOR);
        ExtractableResponse<Response> 신분당선응답 = 지하철_노선_생성_요청(신분당선);

        // when
        // 지하철_노선_수정_요청
        Map<String, String> 구분당선 = 노선_파라미터_설정(LINE_BUNDANG_NAME, LINE_BUNDANG_COLOR);

        ExtractableResponse<Response> 노선수정응답 = 지하철_노선_수정_요청(신분당선응답, 구분당선);

        // then
        // 지하철_노선_수정됨
        응답_상태코드_200(노선수정응답);
    }

    @DisplayName("지하철 노선을 제거한다.")
    @Test
    void deleteLine() {
        // given
        // 지하철_노선_등록되어_있음
        Map<String, String> 신분당선 = 노선_파라미터_설정(LINE_DX_NAME, LINE_DX_COLOR);
        ExtractableResponse<Response> 신분당선응답 = 지하철_노선_생성_요청(신분당선);

        // when
        // 지하철_노선_제거_요청
        ExtractableResponse<Response> 노선삭제응답 = 지하철_노선_제거_요청(신분당선응답);

        // then
        // 지하철_노선_삭제됨
        응답_상태코드_204(노선삭제응답);
    }

    private ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, String> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post(BASE_URL)
                .then().log().all()
                .extract();
    }

    private Map<String, String> 노선_파라미터_설정(String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_NAME, name);
        params.put(PARAM_COLOR, color);
        return params;
    }

    private ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(BASE_URL)
                .then().log().all()
                .extract();
    }

    private void 지하철_노선_목록_포함됨(ExtractableResponse<Response> response, ExtractableResponse<Response> responseWhenPostLineDX, ExtractableResponse<Response> responseWhenPostLineTwo) {
        List<Long> expectedLineIds = Arrays.asList(responseWhenPostLineDX, responseWhenPostLineTwo).stream()
                .map(resp -> Long.parseLong(resp.header(HTTP_HEADER_LOCATION).split("/")[2]))
                .collect(Collectors.toList());
        List<Long> resultLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
                .map(lineResponse -> lineResponse.getId())
                .collect(Collectors.toList());
        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    private ExtractableResponse<Response> 지하철_노선_조회_요청(ExtractableResponse<Response> responseWhenPostLineDX) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(responseWhenPostLineDX.header(HTTP_HEADER_LOCATION))
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_수정_요청(ExtractableResponse<Response> responseWhenPostLineDX, Map<String, String> paramsForUpdating) {
        return RestAssured
                .given().log().all()
                .body(paramsForUpdating)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(responseWhenPostLineDX.header(HTTP_HEADER_LOCATION))
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_제거_요청(ExtractableResponse<Response> responseWhenPostLineDX) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete(responseWhenPostLineDX.header(HTTP_HEADER_LOCATION))
                .then().log().all()
                .extract();
    }

    private void 응답_헤더_로케이션_값_있음(ExtractableResponse<Response> response) {
        assertThat(response.header(HTTP_HEADER_LOCATION)).isNotBlank();
    }

    private void 응답_상태코드_200(ExtractableResponse<Response> responseOfGetAllLines) {
        assertThat(responseOfGetAllLines.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 응답_상태코드_201(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 응답_상태코드_204(ExtractableResponse<Response> responseOfDeleteLine) {
        assertThat(responseOfDeleteLine.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
