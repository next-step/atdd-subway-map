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
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관련 기능")
@Sql("init.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LineAcceptanceTest extends AcceptanceTest {

    @LocalServerPort
    int port;

    StationApiCaller stationApiCaller = new StationApiCaller();

    LineApiCaller lineApiCaller = new LineApiCaller();

    static final String FIRST_LINE_NAME = "신분당선";
    static final String SECOND_LINE_NAME = "분당선";
    static final String FIRST_LINE_COLOR = "bg-red-600";
    static final String SECOND_LINE_COLOR = "bg-green-600";

    @BeforeEach
    public void setUp() {
        stationApiCaller.setPort(port);
        lineApiCaller.setPort(port);

        지하철_역_생성();
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @Test
    void 지하철_노선_등록_테스트() {
        // when
        지하철_노선_생성(FIRST_LINE_NAME, FIRST_LINE_COLOR, 1, 2, 10);

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
        지하철_노선_생성(FIRST_LINE_NAME, FIRST_LINE_COLOR, 1, 2, 10);
        지하철_노선_생성(SECOND_LINE_NAME, SECOND_LINE_COLOR, 1, 3, 9);

        // when
        List<String> subwayLineNames = 지하철_노선_목록_조회();

        // then
        assertThat(subwayLineNames)
                .hasSize(2)
                .contains(FIRST_LINE_NAME, SECOND_LINE_NAME);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @Test
    void 지하철_노선_조회_테스트() {
        // given
        long id = 지하철_노선_생성(FIRST_LINE_NAME, FIRST_LINE_COLOR, 1, 2, 10).jsonPath().getLong("id");

        // when
        Map<String, String> response = 지하철_노선_조회_성공(id);

        // then
        assertThat(response)
                .containsEntry("name", FIRST_LINE_NAME)
                .containsEntry("color", FIRST_LINE_COLOR);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @Test
    void 지하철_노선_수정_테스트() {
        // given
        long id = 지하철_노선_생성(FIRST_LINE_NAME, FIRST_LINE_COLOR, 1, 2, 10).jsonPath().getLong("id");
        String newLineName = "다른분당선";
        String newLineColor = "bg-blue-600";

        // when
        지하철_노선_수정(id, newLineName, newLineColor);

        // then
        Map<String, String> response = 지하철_노선_조회_성공(id);
        assertThat(response)
                .containsEntry("name", newLineName)
                .containsEntry("color", newLineColor);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @Test
    void 지하철_노선_삭제_테스트() {
        // given
        long id = 지하철_노선_생성(FIRST_LINE_NAME, FIRST_LINE_COLOR, 1, 2, 10).jsonPath().getLong("id");

        // when
        지하철_노선_삭제(id);

        // then
        지하철_노선_조회_실패(id);
        List<String> lineNames = 지하철_노선_목록_조회();
        assertThat(lineNames).doesNotContain(FIRST_LINE_NAME);
    }

    void 지하철_역_생성() {
        stationApiCaller.createStation(Map.of("name", "지하철역"));
        stationApiCaller.createStation(Map.of("name", "새로운지하철역"));
        stationApiCaller.createStation(Map.of("name", "또다른지하철역"));
    }

    ExtractableResponse<Response> 지하철_노선_생성(String name, String color, long upStationId, long downStationId, long distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        ExtractableResponse<Response> response = lineApiCaller.createLine(params);

        지하철_API_응답_확인(response.statusCode(), HttpStatus.CREATED);
        assertThat(response.jsonPath().getString("name")).isEqualTo((String) params.get("name"));
        assertThat(response.jsonPath().getString("color")).isEqualTo((String) params.get("color"));
        return response;
    }

    List<String> 지하철_노선_목록_조회() {
        ExtractableResponse<Response> response = lineApiCaller.getLines();

        지하철_API_응답_확인(response.statusCode(), HttpStatus.OK);

        return response.jsonPath().getList("name", String.class);
    }

    Map<String, String> 지하철_노선_조회_성공(long id) {
        ExtractableResponse<Response> response = lineApiCaller.getLineById(id);

        지하철_API_응답_확인(response.statusCode(), HttpStatus.OK);

        return Map.of("name", response.jsonPath().getString("name"),
                "color", response.jsonPath().getString("color"));
    }

    void 지하철_노선_조회_실패(long id) {
        ExtractableResponse<Response> response = lineApiCaller.getLineById(id);

        지하철_API_응답_확인(response.statusCode(), HttpStatus.NO_CONTENT);
    }

    void 지하철_노선_수정(long id, String name, String color) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        ExtractableResponse<Response> response = lineApiCaller.modifyLineById(id, params);

        지하철_API_응답_확인(response.statusCode(), HttpStatus.OK);
    }

    void 지하철_노선_삭제(long id) {
        ExtractableResponse<Response> response = lineApiCaller.deleteLineById(id);

        지하철_API_응답_확인(response.statusCode(), HttpStatus.NO_CONTENT);
    }
}
