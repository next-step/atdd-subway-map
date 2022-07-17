package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.caller.LineApiCaller;
import nextstep.subway.acceptance.caller.SectionApiCaller;
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

@DisplayName("지하철 구간 관련 기능")
@Sql("init.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SectionAcceptanceTest extends AcceptanceTest {

    @LocalServerPort
    int port;

    private long LINE_ID;

    StationApiCaller stationApiCaller = new StationApiCaller();
    LineApiCaller lineApiCaller = new LineApiCaller();
    SectionApiCaller sectionApiCaller = new SectionApiCaller();

    @BeforeEach
    public void setUp() {
        stationApiCaller.setPort(port);
        lineApiCaller.setPort(port);
        sectionApiCaller.setPort(port);

        지하철_역_생성();
        지하철_노선과_구간_생성();
    }

    @Test
    void 지하철_노선_구간_등록_성공_테스트() {
        // when
        final long upStationId = 3;
        final long downStationId = 2;
        지하철_구간_생성(LINE_ID, upStationId, downStationId, 3, true);

        // then
        Map<String, List<Long>> response = 지하철_구간_조회_성공(LINE_ID);
        assertThat(response.get("upStationId")).contains(upStationId);
        assertThat(response.get("downStationId")).contains(downStationId);
    }

    @Test
    void 지하철_노선_구간_등록_실패_테스트() {
        // when
        final long upStationId = 4;
        final long downStationId = 2;
        지하철_구간_생성(LINE_ID, upStationId, downStationId, 3, false);

        // then
        Map<String, List<Long>> response = 지하철_구간_조회_성공(LINE_ID);
        assertThat(response.get("upStationId")).doesNotContain(upStationId);
        assertThat(response.get("downStationId")).doesNotContain(downStationId);
    }

    @Test
    void 지하철_노선_구간_삭제_성공_테스트() {
        // given
        long deleteStationId = 2;
        지하철_구간_생성(LINE_ID, 3, deleteStationId, 10, true);

        // when
        지하철_구간_삭제(LINE_ID, deleteStationId, true);

        // then
        Map<String, List<Long>> response = 지하철_구간_조회_성공(LINE_ID);
        assertThat(response.get("downStationId")).doesNotContain(deleteStationId);
    }

    @Test
    void 지하철_노선_구간_삭제_실패_테스트() {
        // given
        long deleteStationId = 3;

        // when
        지하철_구간_삭제(LINE_ID, deleteStationId, false);

        // then
        Map<String, List<Long>> response = 지하철_구간_조회_성공(LINE_ID);
        assertThat(response.get("downStationId")).contains(deleteStationId);
    }

    void 지하철_역_생성() {
        stationApiCaller.createStation(Map.of("name", "A역"));
        stationApiCaller.createStation(Map.of("name", "B역"));
        stationApiCaller.createStation(Map.of("name", "C역"));
        stationApiCaller.createStation(Map.of("name", "D역"));
    }

    void 지하철_노선과_구간_생성() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "신분당선");
        params.put("color", "bg-red-600");
        params.put("upStationId", 1);
        params.put("downStationId", 3);
        params.put("distance", 7);

        LINE_ID = lineApiCaller.createLine(params).jsonPath().getLong("id");
        sectionApiCaller.createLineSectionById(LINE_ID, params);
    }

    void 지하철_구간_생성(long lineId, long upStationId, long downStationId, long distance, boolean isSuccess) {
        Map<String, Object> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);

        ExtractableResponse<Response> response = sectionApiCaller.createLineSectionById(lineId, params);

        if (isSuccess) {
            지하철_API_응답_확인(response.statusCode(), HttpStatus.OK);
        } else {
            지하철_API_응답_확인(response.statusCode(), HttpStatus.BAD_REQUEST);
        }
    }

    void 지하철_구간_삭제(long lineId, long stationId, boolean isSuccess) {
        ExtractableResponse<Response> response = sectionApiCaller.deleteLineSectionById(lineId, stationId);

        if (isSuccess) {
            지하철_API_응답_확인(response.statusCode(), HttpStatus.NO_CONTENT);
        } else {
            지하철_API_응답_확인(response.statusCode(), HttpStatus.BAD_REQUEST);
        }
    }

    Map<String, List<Long>> 지하철_구간_조회_성공(long lineId) {
        ExtractableResponse<Response> response = sectionApiCaller.getLineSectionById(lineId);

        지하철_API_응답_확인(response.statusCode(), HttpStatus.OK);
        return Map.of(
                "upStationId", response.jsonPath().getList("upStationId", Long.class),
                "downStationId", response.jsonPath().getList("downStationId", Long.class)
        );
    }
}
