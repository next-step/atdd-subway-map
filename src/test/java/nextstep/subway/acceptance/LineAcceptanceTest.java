package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        Map<String, String> params = 지하철_노선_파라미터_생성("신분당선", "bg-red-600");
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(params);

        // then
        지하철_노선_생성_완료(response);
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
        String 신분당선 = "신분당선";
        String 호선2 = "2호선";
        Map<String, String> params1 = 지하철_노선_파라미터_생성(신분당선, "bg-red-600");
        Map<String, String> params2 = 지하철_노선_파라미터_생성(호선2, "bg-green-600");
        지하철_노선_생성_요청(params1);
        지하철_노선_생성_요청(params2);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청("/lines");

        // then
        지하철_노선_조회_완료(response);
        assertThat(response.jsonPath().getList("name")).contains(신분당선, 호선2);
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
        String 신분당선 = "신분당선";
        Map<String, String> params = 지하철_노선_파라미터_생성("신분당선", "bg-red-600");
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(params);
        String uri = createResponse.header("Location");

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회_요청(uri);

        // then
        지하철_노선_조회_완료(response);
        assertThat(response.jsonPath().getString("name")).isEqualTo(신분당선);
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
        Map<String, String> createParams = 지하철_노선_파라미터_생성("신분당선", "bg-red-600");
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(createParams);
        String uri = createResponse.header("Location");

        // when
        String 구분당선 = "구분당선";
        Map<String, String> updateParams = 지하철_노선_파라미터_생성(구분당선, "bg-blue-600");
        ExtractableResponse<Response> response = 지하철_노선_수정_요청(uri, updateParams);

        // then
        지하철_노선_수정_완료(response);
        assertThat(response.jsonPath().getString("name")).isEqualTo(구분당선);
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
        Map<String, String> params = 지하철_노선_파라미터_생성("신분당선", "bg-red-600");
        ExtractableResponse<Response> createResponse = 지하철_노선_생성_요청(params);
        String uri = createResponse.header("Location");

        // when
        ExtractableResponse<Response> response = 지하철_노선_삭제_요청(uri);

        // then
        지하철_노선_삭제_완료(response);
    }

    /**
     * Scenario: 중복이름으로 지하철 노선 생성
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 이름으로 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("중복이름으로 지하철 노선 생성")
    @Test
    void createLineWithDuplicateName() {
        // given
        Map<String, String> params = 지하철_노선_파라미터_생성("신분당선", "bg-red-600");
        지하철_노선_생성_요청(params);

        // when
        ExtractableResponse<Response> response = 지하철_노선_생성_요청(params);

        // then
        지하철_노선_생성_실패(response);
    }

    private Map<String, String> 지하철_노선_파라미터_생성(String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        return params;
    }

    private ExtractableResponse<Response> 지하철_노선_생성_요청(Map<String, String> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/lines")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_조회_요청(String uri) {
        return RestAssured.given().log().all()
                .when()
                .get(uri)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_수정_요청(String uri, Map<String, String> params) {
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put(uri)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 지하철_노선_삭제_요청(String uri) {
        return RestAssured.given().log().all()
                .when()
                .delete(uri)
                .then().log().all()
                .extract();
    }

    private void 지하철_노선_응답_상태_검증(ExtractableResponse<Response> response, HttpStatus status) {
        assertThat(response.statusCode()).isEqualTo(status.value());
    }

    private void 지하철_노선_생성_완료(ExtractableResponse<Response> response) {
        지하철_노선_응답_상태_검증(response, HttpStatus.CREATED);
    }

    private void 지하철_노선_조회_완료(ExtractableResponse<Response> response) {
        지하철_노선_응답_상태_검증(response, HttpStatus.OK);
    }

    private void 지하철_노선_삭제_완료(ExtractableResponse<Response> response) {
        지하철_노선_응답_상태_검증(response, HttpStatus.NO_CONTENT);
    }

    private void 지하철_노선_수정_완료(ExtractableResponse<Response> response) {
        지하철_노선_응답_상태_검증(response, HttpStatus.OK);
    }

    private void 지하철_노선_생성_실패(ExtractableResponse<Response> response) {
        지하철_노선_응답_상태_검증(response, HttpStatus.BAD_REQUEST);
    }
}
