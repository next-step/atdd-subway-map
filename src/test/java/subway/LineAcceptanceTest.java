package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {
    private final String LINE_API_PATH = "/lines";
    private final String LINE_SHINBUNDANG = "신분당선";
    private final String LINE_1 = "1호선";
    private final String COLOR_CODE_RED = "bg-red-600";
    private final String COLOR_CODE_BLUE = "bg-blue-600";
    private final String PARAM_NAME = "name";
    private final String PARAM_LINE_ID = "id";
    private final String PARAM_COLOR = "color";

    /**
     * 지하철 노선 생성
     * 노선 생성 시 상행종점역과 하행종점역을 등록
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void 지하철_노선_생성_테스트() {
        // when & then
        지하철_노선_생성(LINE_SHINBUNDANG, COLOR_CODE_RED);

        // then
        List<String> lineNames = getJsonPathList(지하철_노선_목록_조회(), PARAM_NAME);
        assertThat(lineNames).containsAnyOf(LINE_SHINBUNDANG);
    }

    /**
     * 지하철 노선 목록 조회
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다
     */
    @DisplayName("지하철 노선 목록을 조회한다.")
    @Test
    void 지하철_노선_목록_조회_테스트() {
        // given
        지하철_노선_생성(LINE_SHINBUNDANG, COLOR_CODE_RED);
        지하철_노선_생성(LINE_1, COLOR_CODE_BLUE);

        // when
        List<String> lineNames = getJsonPathList(지하철_노선_목록_조회(), PARAM_NAME);

        // then
        assertThat(lineNames).containsOnly(LINE_SHINBUNDANG, LINE_1);
    }

    /**
     * 지하철 노선 조회
     * 노선 조회시 포함된 역 목록이 함께 응답
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다
     */
    @DisplayName("지하철 노선을 조회한다.")
    @Test
    void 지하철_노선_조회_테스트() {
        // given
        long lineId = 지하철_노선_생성(LINE_SHINBUNDANG, COLOR_CODE_RED).jsonPath().getLong(PARAM_LINE_ID);

        // when
        String lineName = 지하철_노선_조회(lineId).jsonPath().getString(PARAM_NAME);

        // then
        assertThat(lineName).isEqualTo(LINE_SHINBUNDANG);
    }

    /**
     * 지하철 노선 수정
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void 지하철_노선_수정_테스트() {
        // given
        long lineId = 지하철_노선_생성(LINE_SHINBUNDANG, COLOR_CODE_RED).jsonPath().getLong(PARAM_LINE_ID);

        // when
        지하철_노선_수정(lineId, LINE_1, COLOR_CODE_BLUE);

        // then
        String lineName = 지하철_노선_조회(lineId).jsonPath().getString(PARAM_NAME);
        assertThat(lineName).isEqualTo(LINE_1);
    }

    /**
     * 지하철 노선 삭제
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void 지하철_노선_삭제_테스트() {
        // given
        long lineId = 지하철_노선_생성(LINE_SHINBUNDANG, COLOR_CODE_RED).jsonPath().getLong(PARAM_LINE_ID);

        // when
        지하철_노선_삭제(lineId);

        // then
        List<String> lineNames = getJsonPathList(지하철_노선_목록_조회(), PARAM_NAME);
        assertThat(lineNames).doesNotContainAnyElementsOf(List.of(LINE_SHINBUNDANG));
    }

    private List<String> getJsonPathList(ExtractableResponse<Response> response, String path) {
        return response.jsonPath().getList(path, String.class);
    }

    private Map<String, String> generateLineParams(String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_NAME, name);
        params.put(PARAM_COLOR, color);
        return params;
    }

    private ExtractableResponse<Response> 지하철_노선_생성(String name, String color) {
        Map<String, String> params = generateLineParams(name, color);
        ExtractableResponse<Response> response = HttpRequest.sendPostRequest(LINE_API_PATH, params);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return response;
    }

    private ExtractableResponse<Response> 지하철_노선_목록_조회() {
        ExtractableResponse<Response> response = HttpRequest.sendGetRequest(LINE_API_PATH);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        return response;
    }

    private ExtractableResponse<Response> 지하철_노선_조회(long lineId) {
        ExtractableResponse<Response> response = HttpRequest.sendGetRequest(LINE_API_PATH + "/" + lineId);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        return response;
    }

    private void 지하철_노선_수정(long lineId, String name, String color) {
        Map<String, String> params = generateLineParams(name, color);
        ExtractableResponse<Response> response = HttpRequest.sendPutRequest(LINE_API_PATH + "/" + lineId, params);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 지하철_노선_삭제(long lineId) {
        ExtractableResponse<Response> response = HttpRequest.sendDeleteRequest(LINE_API_PATH + "/" + lineId);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
