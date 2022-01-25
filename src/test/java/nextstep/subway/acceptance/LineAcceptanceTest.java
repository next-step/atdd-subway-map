package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.LineTestRequest.*;
import static nextstep.subway.acceptance.LineAcceptanceTest.노선.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    private static final String 이름 = "name";
    private static final String 색 = "color";

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // given
        Map<String, String> params = createParams(신분당선);

        // when
        ExtractableResponse<Response> response = 노선_생성_요청(params);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
        assertThat(response.header(HttpHeaders.LOCATION)).isNotBlank();
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
        Map<String, String> params1 = createParams(신분당선);
        Map<String, String> params2 = createParams(_2호선);

        노선_생성_요청(params1);
        노선_생성_요청(params2);

        // when
        ExtractableResponse<Response> response = 노선_목록_조회_요청();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> lineNames = response.jsonPath().getList(이름);
        assertThat(lineNames).containsExactly(신분당선.이름, _2호선.이름);
        List<String> lineColors = response.jsonPath().getList(색);
        assertThat(lineColors).containsExactly(신분당선.색, _2호선.색);
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
        Map<String, String> params = createParams(신분당선);

        노선_생성_요청(params);

        // when
        ExtractableResponse<Response> response = 노선_조회_요청(1L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        String name = response.jsonPath().get(이름);
        assertThat(name).isEqualTo(신분당선.이름);
        String color = response.jsonPath().get(색);
        assertThat(color).isEqualTo(신분당선.색);
    }

    /**
     * When 없는 지하철 노선 조회를 요청 하면
     * Then 404 응답을 받는다
     */
    @DisplayName("지하철 노선 조회 - 없는 노선")
    @Test
    void getInvalidLine() {
        // when
        ExtractableResponse<Response> response = 노선_조회_요청(999L);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정 - 존재 하는 경우 수정")
    @Test
    void updateLine() {
        // given
        Map<String, String> params = createParams(구분당선);

        ExtractableResponse<Response> createResponse = 노선_생성_요청(params);

        // when
        String uri = createResponse.header(HttpHeaders.LOCATION);
        ExtractableResponse<Response> response = 노선_변경_요청(params, uri);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    /**
     * When 없는 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선이 생성이 성공한다.
     */
    @DisplayName("지하철 노선 수정 - 없는 경우 NOT FOUND")
    @Test
    void updateNotExistLine() {
        Map<String, String> params = createParams(구분당선);

        // when
        ExtractableResponse<Response> response = 노선_변경_요청(params, "/lines/1");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
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
        Map<String, String> params = createParams(구분당선);

        ExtractableResponse<Response> createResponse = 노선_생성_요청(params);

        // when
        String uri = createResponse.header(HttpHeaders.LOCATION);
        ExtractableResponse<Response> response = 노선_삭제_요청(params, uri);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private Map<String, String> createParams(노선 노선) {
        Map<String, String> params = new HashMap<>();
        params.put(이름, 노선.이름);
        params.put(색, 노선.색);
        return params;
    }

    enum 노선 {
        신분당선("신분당선", "bg-red-600"),
        구분당선("구분당선", "bg-blue-600"),
        _2호선("2호선", "bg-green-600");

        public final String 이름;
        public final String 색;

        노선(String 이름, String 색) {
            this.이름 = 이름;
            this.색 = 색;
        }
    }
}
