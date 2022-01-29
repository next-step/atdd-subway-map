package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.StationStepDefinition.*;

@DisplayName("지하철역 관리 기능")
class StationAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 성공한다.
     */
    @DisplayName("지하철역 생성")
    @Test
    void createStation() {
        // when
        String 강남역 = "강남역";
        ExtractableResponse<Response> createResponse = 지하철역_생성_요청(강남역);

        // then
        지하철역_생성_완료(createResponse);
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * Given 새로운 지하철역 생성을 요청 하고
     * When 지하철역 목록 조회를 요청 하면
     * Then 두 지하철역이 포함된 지하철역 목록을 응답받는다
     */
    @DisplayName("지하철역 목록 조회")
    @Test
    void getStations() {
        /// given
        String 강남역 = "강남역";
        String 역삼역 = "역삼역";
        지하철역_생성_요청(강남역);
        지하철역_생성_요청(역삼역);

        // when
        ExtractableResponse<Response> response = 지하철역_조회_요청("/stations");

        지하철역_목록_조회_완료(response, 강남역, 역삼역);
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * When 생성한 지하철역 삭제를 요청 하면
     * Then 생성한 지하철역 삭제가 성공한다.
     */
    @DisplayName("지하철역 삭제")
    @Test
    void deleteStation() {
        // given
        String 강남역 = "강남역";
        ExtractableResponse<Response> createResponse = 지하철역_생성_요청(강남역);

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = 지하철역_삭제_요청(uri);

        // then
        지하철역_삭제_완료(response);
    }

    /**
     * Scenario: 중복이름으로 지하철역 생성
     * Given 지하철역 생성을 요청 하고
     * When 같은 이름으로 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 실패한다.
     */
    @DisplayName("중복이름으로 지하철역 생성")
    @Test
    void createStationWithDuplicateName() {
        // given
        String 강남역 = "강남역";
        ExtractableResponse<Response> createResponse = 지하철역_생성_요청(강남역);

        // when
        ExtractableResponse<Response> response = 지하철역_생성_요청(강남역);

        // then
        지하철역_생성_실패(response);
    }
}
