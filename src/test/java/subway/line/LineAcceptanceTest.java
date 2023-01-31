package subway.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static subway.utils.LineUtil.createLineResultResponse;
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

    }
}
