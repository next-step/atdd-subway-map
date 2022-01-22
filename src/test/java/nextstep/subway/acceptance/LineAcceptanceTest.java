package nextstep.subway.acceptance;

import static java.lang.String.*;
import static org.assertj.core.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    private static final String REQUEST_PATH = "/lines";

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        ExtractableResponse<Response> response = 노선_생성_요청("신분당선", "red");
        
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotEmpty();
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
        final String 구호선 = "9호선";
        final String 신분당선 = "신분당선";

        노선_생성_요청(구호선, "bg-red-600");
        노선_생성_요청(신분당선, "bg-green-600");

        // when
        ExtractableResponse<Response> response = 모든_노선_목록_요청();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList("name")).containsExactly(구호선, 신분당선);
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
        final String 구호선 = "9호선";
        final String 신분당선 = "신분당선";

        final long 구호선_아이디 = 노선_아이디_추출(노선_생성_요청(구호선, "bg-red-600"));
        노선_생성_요청(신분당선, "bg-green-600");

        // when
        final ExtractableResponse<Response> response = 노선_조회_요청(구호선_아이디);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("name")).isEqualTo(구호선);

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
        final String 구호선 = "9호선";
        final long 구호선_아이디 = 노선_아이디_추출(노선_생성_요청(구호선, "bg-red-600"));

        // when
        final String 신분당선 = "신분당선";
        final String 신분당선_색상 = "bg_yellow-600";
        final ExtractableResponse<Response> response = 노선_수정_요청(구호선_아이디, 신분당선, 신분당선_색상);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("name")).isEqualTo(신분당선);
        assertThat(response.jsonPath().getString("color")).isEqualTo(신분당선_색상);
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
        final String 구호선 = "9호선";
        final long 구호선_아이디 = 노선_아이디_추출(노선_생성_요청(구호선, "bg-yellow-600"));

        // when
        final ExtractableResponse<Response> response = 노선_삭제_요청(구호선_아이디);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 노선_삭제_요청(long lineId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(format("%s/%s", REQUEST_PATH, lineId))
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 노선_수정_요청(long lineId, String name, String color) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(요청_본문_생성(name, color))
            .when().put(format("%s/%s", REQUEST_PATH, lineId))
            .then().log().all().extract();
    }

    private ExtractableResponse<Response> 모든_노선_목록_요청() {
        return 노선_조회_요청(REQUEST_PATH);
    }

    private ExtractableResponse<Response> 노선_조회_요청(long lineId) {
        return 노선_조회_요청(format("%s/%s", REQUEST_PATH, lineId));
    }

    private ExtractableResponse<Response> 노선_조회_요청(String requestPath) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get(requestPath)
            .then().log().all().extract();
    }

    private ExtractableResponse<Response> 노선_생성_요청(String lineName, String lineColor) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(요청_본문_생성(lineName, lineColor))
            .when().post(REQUEST_PATH)
            .then().log().all().extract();
    }

    private Map<String, String> 요청_본문_생성(String lineName, String lineColor) {
        final Map<String, String> body = new HashMap<>();
        body.put("name", lineName);
        body.put("color", lineColor);
        return body;
    }

    private long 노선_아이디_추출(ExtractableResponse<Response> response) {
        return response.jsonPath().getLong("id");
    }

}
