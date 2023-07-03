package subway;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StationAcceptanceTest {
    private final String STATION_API_PATH = "/stations";
    private final String LINE_API_PATH = "/lines";
    private final String STATION_GANGNAM = "강남역";
    private final String STATION_SAMSUNG = "삼성역";
    private final String LINE_SHINBUNDANG = "신분당선";
    private final String LINE_1 = "1호선";
    private final String COLOR_CODE_RED = "bg-red-600";
    private final String COLOR_CODE_BLUE = "bg-blue-600";
    private final String PARAM_NAME = "name";
    private final String PARAM_STATION_ID = "id";
    private final String PARAM_LINE_ID = "id";

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다.")
    @Test
    void 지하철_역_생성_테스트() {
        // when & then
        지하철_역_생성(STATION_GANGNAM);

        // then
        List<String> stationNames = getJsonPathList(지하철_역_목록_조회(), PARAM_NAME);
        assertThat(stationNames).containsAnyOf(STATION_GANGNAM);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다.")
    @Test
    void 지하철_역_목록_조회_테스트() {
        // given
        지하철_역_생성(STATION_GANGNAM);
        지하철_역_생성(STATION_SAMSUNG);

        // when
        List<String> stationNames = getJsonPathList(지하철_역_목록_조회(), PARAM_NAME);

        // then
        assertThat(stationNames).containsOnly(STATION_GANGNAM, STATION_SAMSUNG);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 삭제한다.")
    @Test
    void 지하철_역_삭제_테스트() {
        // given
        long stationId = 지하철_역_생성(STATION_GANGNAM).jsonPath().getLong(PARAM_STATION_ID);

        // when
        지하철_역_삭제(stationId);

        // then
        List<String> stationNames = getJsonPathList(지하철_역_목록_조회(), PARAM_NAME);
        assertThat(stationNames).doesNotContainAnyElementsOf(List.of(STATION_GANGNAM));
    }

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
        지하철_노선_생성(LINE_SHINBUNDANG, COLOR_CODE_RED,1, 2);

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
        지하철_노선_생성(LINE_SHINBUNDANG, COLOR_CODE_RED,1, 2);
        지하철_노선_생성(LINE_1, COLOR_CODE_RED,1, 2);

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
        지하철_노선_생성(LINE_SHINBUNDANG, COLOR_CODE_RED,1, 2, 10);

        // when
        List<String> lineNames = getJsonPathList(지하철_노선_조회(1), PARAM_NAME);

        // then
        assertThat(lineNames).containsOnly(LINE_SHINBUNDANG);
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
        지하철_노선_생성(LINE_SHINBUNDANG, COLOR_CODE_RED,1, 2, 10);

        // when
        지하철_노선_수정(1, LINE_1, COLOR_CODE_BLUE);

        // then
        List<String> lineNames = getJsonPathList(지하철_노선_조회(1), PARAM_NAME);
        assertThat(lineNames).containsOnly(LINE_1);
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
        long lineId = 지하철_노선_생성(LINE_SHINBUNDANG, COLOR_CODE_RED,1, 2, 10).jsonPath().getLong(PARAM_LINE_ID);

        // when
        지하철_노선_삭제(lineId);

        // then
        List<String> lineNames = getJsonPathList(지하철_노선_목록_조회(), PARAM_NAME);
        assertThat(lineNames).doesNotContainAnyElementsOf(List.of(LINE_SHINBUNDANG));
    }

    private ExtractableResponse<Response> 지하철_역_생성(String name) {
        Map<String, String> params = generateStationParams(name);
        ExtractableResponse<Response> response = HttpRequest.sendPostRequest(STATION_API_PATH, params);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return response;
    }

    private ExtractableResponse<Response> 지하철_역_목록_조회() {
        ExtractableResponse<Response> response = HttpRequest.sendGetRequest(STATION_API_PATH);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        return response;
    }

    private void 지하철_역_삭제(long stationId) {
        ExtractableResponse<Response> response = HttpRequest.sendDeleteRequest(STATION_API_PATH + "/" + stationId);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private Map<String, String> generateStationParams(String name) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_NAME, name);
        return params;
    }

    private List<String> getJsonPathList(ExtractableResponse<Response> response, String path) {
        return response.jsonPath().getList(path, String.class);
    }

    private Map<String, String> generateLineParams(String name) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_NAME, name);
        return params;
    }

    private ExtractableResponse<Response> 지하철_노선_생성(String name, String color, long upStationId, long downStationId, int distance) {
        Map<String, String> params = generateLineParams(name);
        ExtractableResponse<Response> response = HttpRequest.sendPostRequest(LINE_API_PATH, params);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return response;
    }

    private ExtractableResponse<Response> 지하철_노선_목록_조회() {
        ExtractableResponse<Response> response = HttpRequest.sendGetRequest(STATION_API_PATH);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        return response;
    }

    private ExtractableResponse<Response> 지하철_노선_조회(long lineId) {
        ExtractableResponse<Response> response = HttpRequest.sendGetRequest(STATION_API_PATH);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        return response;
    }

    private ExtractableResponse<Response> 지하철_노선_수정(long lineId, String name, String color) {
        Map<String, String> params = generateLineParams(name);
        ExtractableResponse<Response> response = HttpRequest.sendPostRequest(LINE_API_PATH, params);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        return response;
    }

    private void 지하철_노선_삭제(long stationId) {
        ExtractableResponse<Response> response = HttpRequest.sendDeleteRequest(STATION_API_PATH + "/" + stationId);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}