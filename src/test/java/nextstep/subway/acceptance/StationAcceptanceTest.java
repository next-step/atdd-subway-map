package nextstep.subway.acceptance;

import nextstep.subway.fixture.StationFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.step.StationStep.*;

@DisplayName("지하철역 관리 기능")
class StationAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 성공한다.
     */
    @DisplayName("지하철역 생성")
    @Test
    void createStation() {
        // given
        var params = StationFixture.강남역;

        // when
        var response = 역_생성_요청(params);

        // then
        역_생성_완료(response);
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
        var params1 = StationFixture.강남역;
        역_생성_요청(params1);

        var params2 = StationFixture.역삼역;
        역_생성_요청(params2);

        // when
        var response = 역_목록_조회_요청();

        역_목록_조회_완료(response, params1, params2);
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
        var params = StationFixture.강남역;
        var createResponse = 역_생성_요청(params);

        // when
        String uri = createResponse.header("Location");
        var response = 역_삭제_요청(uri);

        // then
        역_삭제_완료(response);
    }

    /**
     * Given 지하철역 생성을 요청 하고
     * When 같은 이름으로 지하철역 생성을 요청 하면
     * Then 지하철역 생성이 실패한다.
     */
    @DisplayName("중복이름으로 지하철역 생성")
    @Test
    void createStationWithDuplicateName() {
        // given
        var params = StationFixture.강남역;
        역_생성_요청(params);

        // when
        var response = 역_생성_요청(params);

        // then
        중복된_역_생성_예외(response);
    }

}
