package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.caller.LineApiCaller;
import nextstep.subway.acceptance.caller.StationApiCaller;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LineAcceptanceTest {

    @LocalServerPort
    int port;

    StationApiCaller stationApiCaller = new StationApiCaller();

    LineApiCaller lineApiCaller = new LineApiCaller();

    static final String FIRST_LINE_NAME = "신분당선";
    static final String SECOND_LINE_NAME = "분당선";

    @BeforeEach
    public void setUp() {
        stationApiCaller.setPort(port);
        lineApiCaller.setPort(port);

        setStations();
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @Test
    void 지하철_노선_등록_테스트() {
        // when
        지하철_노선_생성(FIRST_LINE_NAME, "bg-red-600", 1, 2, 10);

        // then
        List<String> subwayLineNames = 지하철_노선_목록_조회();
        assertThat(subwayLineNames).containsAnyOf(FIRST_LINE_NAME);
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @Test
    void 지하철_노선_목록_조회_테스트() {
        // given
        지하철_노선_생성(FIRST_LINE_NAME, "bg-red-600", 1, 2, 10);
        지하철_노선_생성(SECOND_LINE_NAME, "bg-green-600", 1, 3, 9);

        // when
        List<String> subwayLineNames = 지하철_노선_목록_조회();

        // then
        assertThat(subwayLineNames).hasSize(2).contains(FIRST_LINE_NAME, SECOND_LINE_NAME);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @Test
    void 지하철_노선_조회_테스트() {
        // given
        long id = 지하철_노선_생성(FIRST_LINE_NAME, "bg-red-600", 1, 2, 10).jsonPath().getLong("id");

        // when
        String lineName = 지하철_노선_조회(id);

        // then
        assertThat(lineName).isEqualTo(FIRST_LINE_NAME);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 수정한다.")
    @Test
    void modifySubwayLine() {
        // given
        long id = 지하철_노선_생성(FIRST_LINE_NAME, "bg-red-600", 1, 2, 10).jsonPath().getLong("id");
        String newLineName = "다른분당선";
        String newLineColor = "bg-blue-600";

        // when
        지하철_노선_수정(id, newLineName, newLineColor);

        // then
        String lineName = 지하철_노선_조회(id);
        assertThat(lineName).isEqualTo(newLineName);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 삭제한다.")
    @Test
    void createStation() {
        // given
        long id = 지하철_노선_생성(FIRST_LINE_NAME, "bg-red-600", 1, 2, 10).jsonPath().getLong("id");

        // when
        지하철_노선_삭제(id);

        // then
        String lineName = 지하철_노선_조회(id);
        assertThat(lineName).isNull();
    }

    void setStations() {
        stationApiCaller.createStation(Map.of("name", "지하쳘역"));
        stationApiCaller.createStation(Map.of("name", "새로운지하쳘역"));
        stationApiCaller.createStation(Map.of("name", "또다른지하쳘역"));
    }

    ExtractableResponse<Response> 지하철_노선_생성(String name, String color, int upStationId, int downStationId, int distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        ExtractableResponse<Response> response = lineApiCaller.createLine(params);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.jsonPath().getString("name")).isEqualTo((String) params.get("name"));
        assertThat(response.jsonPath().getString("color")).isEqualTo((String) params.get("color"));
        return response;
    }

    List<String> 지하철_노선_목록_조회() {
        ExtractableResponse<Response> response = lineApiCaller.getLines();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        return response.jsonPath().getList("name", String.class);
    }

    String 지하철_노선_조회(long id) {
        ExtractableResponse<Response> response = lineApiCaller.getLine(id);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        return response.jsonPath().getString("name");
    }

    void 지하철_노선_수정(long id, String name, String color) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        ExtractableResponse<Response> response = lineApiCaller.modifyLine(id, params);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    void 지하철_노선_삭제(long id) {
        ExtractableResponse<Response> response = lineApiCaller.deleteLine(id);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
