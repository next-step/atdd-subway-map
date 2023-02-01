package subway.line;

import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static subway.utils.AssertUtil.assertEqualToLine;
import static subway.utils.AssertUtil.assertEqualToNames;
import static subway.utils.LineUtil.*;
import static subway.utils.StationUtil.createStationResultResponse;

@DisplayName("지하철 노선 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LineAcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
     */
    @Test
    @DisplayName("지하철노선 생성")
    void createLine() {
        // when
        createStationResultResponse("지하철역");
        createStationResultResponse("새로운지하철역");

        createLineResultResponse("신분당선", "bg-red-600", 1L, 2L, 10);

        // then
        List<String> lineNames = showLinesResultResponse().getList("name", String.class);
        assertEqualToNames(lineNames, "신분당선");
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @Test
    @DisplayName("지하철노선 목록 조회")
    void showLines() {
        // given
        Long stationId = createStationResultResponse("지하철역").getLong("id");
        Long newStationId = createStationResultResponse("새로운지하철역").getLong("id");
        Long otherStationId = createStationResultResponse("또다른지하철역").getLong("id");

        createLineResultResponse("신분당선", "bg-red-600", stationId, newStationId, 10);
        createLineResultResponse("분당선", "bg-green-600", stationId, otherStationId, 10);

        // when
        List<String> lineNames = showLinesResultResponse().getList("name", String.class);

        // then
        assertEqualToNames(lineNames, "신분당선", "분당선");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @Test
    @DisplayName("지하철노선 조회")
    void showLine() {
        // given
        createStationResultResponse("지하철역");
        createStationResultResponse("새로운지하철역");

        Long lineId = createLineResultResponse("신분당선", "bg-red-600", 1L, 2L, 10).getLong("id");

        // when
        JsonPath line = showLineResultResponse(lineId);

        // then
        assertEqualToLine(line, "신분당선", "bg-red-600", "지하철역", "새로운지하철역");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다.
     */
    @Test
    @DisplayName("지하철노선 수정")
    void updateLine() {
        // given
        createStationResultResponse("지하철역");
        createStationResultResponse("새로운지하철역");

        Long lineId = createLineResultResponse("신분당선", "bg-red-600", 1L, 2L, 10).getLong("id");

        // when
        updateLineResult(lineId, "다른분당선", "bg-red-600");

        // then
        JsonPath line = showLineResultResponse(lineId);
        assertEqualToLine(line, "다른분당선", "bg-red-600", "지하철역", "새로운지하철역");
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다.
     */
    @Test
    @DisplayName("지하철노선 삭제")
    void deleteLine() {
        // given
        createStationResultResponse("지하철역");
        createStationResultResponse("새로운지하철역");

        Long lineId = createLineResultResponse("신분당선", "bg-red-600", 1L, 2L, 10).getLong("id");

        // when
        deleteLineResult(lineId);

        // then
        List<String> lineNames = showLinesResultResponse().getList("name", String.class);
        assertEqualToNames(lineNames);
    }
}
