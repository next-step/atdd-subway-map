package subway.section;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.common.AcceptanceTest;

import static subway.utils.AssertUtil.assertEqualToSections;
import static subway.utils.AssertUtil.assertSuccessOk;
import static subway.utils.LineUtil.createLineResultResponse;
import static subway.utils.LineUtil.showLineResultResponse;
import static subway.utils.SectionUtil.*;
import static subway.utils.StationUtil.createStationResultResponse;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    /**
     * Given 노선을 생성한다.
     * When 노선에 새로운 구간을 등록한다.
     * Then 노선 구간 목록에서 찾을 수 있다.
     */
    @Test
    @DisplayName("지하철 구간 등록")
    void addSection() {
        // Given
        Long upStationId = createStationResultResponse("강남역").getLong("id");
        Long downStationId = createStationResultResponse("역삼역").getLong("id");
        Long newStationId = createStationResultResponse("선릉역").getLong("id");

        Long lineId = createLineResultResponse("2호선", "bg-green-600", upStationId, downStationId, 7).getLong("id");

        // When
        ExtractableResponse<Response> response = addSectionResponse(lineId, downStationId, newStationId, 3);
        assertSuccessOk(response);

        // Then
        JsonPath line = showLineResultResponse(lineId);
        assertEqualToSections(line, new String[]{"역삼역", "선릉역"}, new String[]{"강남역", "역삼역"}, 7, 3);
    }
}
