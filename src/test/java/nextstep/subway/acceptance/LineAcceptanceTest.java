package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static nextstep.subway.utils.RestAssuredRequest.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
    private static final String LINES_PATH = "/lines";

    private Map<String, String> 신분당선;
    private Map<String, String> 이호선;
    private Map<String, String> 구분당선;

    @BeforeEach
    void initParam() {
        신분당선 = new HashMap<>();
        신분당선.put("name", "신분당선");
        신분당선.put("color", "bg-red-600");

        이호선 = new HashMap<>();
        이호선.put("name", "2호선");
        이호선.put("color", "bg-green-600");

        구분당선 = new HashMap<>();
        구분당선.put("name", "구분당선");
        구분당선.put("color", "bg-blue-600");
    }

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(신분당선);

        // then
        지하철_노선_생성됨(response);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // given
        ExtractableResponse<Response> createResponse1 = 지하철_노선_등록되어_있음(신분당선);
        ExtractableResponse<Response> createResponse2 = 지하철_노선_등록되어_있음(이호선);

        // when
        ExtractableResponse<Response> response = 지하철_노선_목록_조회_요청();

        // then
        지하철_노선_목록_조회됨(response, createResponse1, createResponse2);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_등록되어_있음(신분당선);

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(uri);

        // then
        지하철_노선_조회됨(response, createResponse);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_등록되어_있음(신분당선);

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(uri, 구분당선);

        // then
        ExtractableResponse<Response> showResponse = 지하철_노선_조회_요청(uri);
        지하철_노선_수정됨(response, showResponse, 구분당선);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(신분당선);

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = 지하철_노선_삭제_요청(uri);

        // then
        지하철_노선_삭제됨(response);
    }

    /**
     * Scenario: 중복이름으로 지하철 노선 생성
     * Given 지하철 노선 생성 요청 하고
     * When 같은 이름으로 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("지하철 노선 중복이름으로 생성")
    @Test
    void createLineWithDuplicateName() {
        // given
        지하철_노선_등록되어_있음(신분당선);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(신분당선);

        // then
        지하철_노선_이름_중복됨(response);
    }

    private ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, String> params) {
        return post(params, LINES_PATH);
    }

    private ExtractableResponse<Response> 지하철_노선_목록_조회_요청() {
        return get(LINES_PATH);
    }

    private ExtractableResponse<Response> 지하철_노선_조회_요청(String uri) {
        return get(uri);
    }

    private ExtractableResponse<Response> 지하철_노선_수정_요청(String uri, Map<String, String> params) {
        return put(uri, params);
    }

    private ExtractableResponse<Response> 지하철_노선_삭제_요청(String uri) {
        return delete(uri);
    }

    private ExtractableResponse<Response> 지하철_노선_등록되어_있음(Map<String, String> params) {
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(params);
        지하철_노선_생성됨(response);

        return response;
    }

    private void 지하철_노선_생성됨(ExtractableResponse<Response> response) {
        응답_요청_확인(response, HttpStatus.CREATED);
        assertThat(response.header("Location")).isNotBlank();
    }

    private void 지하철_노선_목록_조회됨(ExtractableResponse<Response> response, ExtractableResponse<Response> ... createResponses) {
        List<String> createdNames = Arrays.stream(createResponses)
                .map(createResponse -> createResponse.jsonPath().getString("name"))
                .collect(Collectors.toList());
        List<String> createdColors = Arrays.stream(createResponses)
                .map(createResponse -> createResponse.jsonPath().getString("color"))
                .collect(Collectors.toList());

        응답_요청_확인(response, HttpStatus.OK);
        assertThat(response.jsonPath().getList("name")).isEqualTo(createdNames);
        assertThat(response.jsonPath().getList("color")).isEqualTo(createdColors);
    }

    private void 지하철_노선_조회됨(ExtractableResponse<Response> response, ExtractableResponse<Response> createResponse) {
        응답_요청_확인(response, HttpStatus.OK);
        assertThat(response.jsonPath().getString("name")).isEqualTo(createResponse.jsonPath().getString("name"));
    }

    private void 지하철_노선_수정됨(ExtractableResponse<Response> response, ExtractableResponse<Response> showResponse, Map<String, String> updateParams) {
        응답_요청_확인(response, HttpStatus.OK);
        응답_요청_확인(showResponse, HttpStatus.OK);
        assertThat(showResponse.jsonPath().getString("name")).isEqualTo(updateParams.get("name"));
        assertThat(showResponse.jsonPath().getString("color")).isEqualTo(updateParams.get("color"));
    }

    private void 지하철_노선_삭제됨(ExtractableResponse<Response> response) {
        응답_요청_확인(response, HttpStatus.NO_CONTENT);
    }

    private void 지하철_노선_이름_중복됨(ExtractableResponse<Response> response) {
        응답_요청_확인(response, HttpStatus.CONFLICT);
    }

    private void 응답_요청_확인(ExtractableResponse<Response> response, HttpStatus httpStatus) {
        assertThat(response.statusCode()).isEqualTo(httpStatus.value());
    }
}
