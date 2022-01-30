package nextstep.subway.acceptance;

import nextstep.subway.acceptance.step.LineStep;
import nextstep.subway.fixture.LineFixture;
import nextstep.subway.fixture.StationFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.step.LineStep.*;
import static nextstep.subway.acceptance.step.StationStep.역_생성_요청;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // given
        역_생성_요청(StationFixture.신논현역);
        역_생성_요청(StationFixture.강남역);

        var 노선1 = LineFixture.신분당선;

        // when
        var 노선_생성_응답 = 노선_생성_요청(노선1);

        // then
        노선_생성_완료(노선_생성_응답);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        // given
        역_생성_요청(StationFixture.신논현역);
        역_생성_요청(StationFixture.강남역);
        역_생성_요청(StationFixture.역삼역);

        var 노선1 = LineFixture.신분당선;
        노선_생성_요청(노선1);

        var 노선2 = LineFixture.이호선;
        노선_생성_요청(노선2);

        // when
        var 노선_목록_조회_응답 = LineStep.노선_목록_조회_요청();

        // then
        노선_목록_조회_완료(노선_목록_조회_응답, 노선1, 노선2);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        // given
        역_생성_요청(StationFixture.신논현역);
        역_생성_요청(StationFixture.강남역);

        var 노선1 = LineFixture.신분당선;
        var 노선_생성_응답 = 노선_생성_요청(노선1);

        // when
        var uri = 노선_생성_응답.header("Location");
        var 노선_조회_응답 = LineStep.노선_조회_요청(uri);

        // then
        노선_조회_완료(노선_조회_응답, 노선1);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        // given
        역_생성_요청(StationFixture.신논현역);
        역_생성_요청(StationFixture.강남역);

        var 노선1 = LineFixture.신분당선;
        var 노선_생성_응답 = 노선_생성_요청(노선1);

        // when
        var 노선2 = LineFixture.이호선;

        var uri = 노선_생성_응답.header("Location");
        var 노선_수정_응답 = LineStep.노선_수정_요청(uri, 노선2);

        // then
        노선_수정_완료(노선_수정_응답, 노선2);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        역_생성_요청(StationFixture.신논현역);
        역_생성_요청(StationFixture.강남역);

        var 노선1 = LineFixture.신분당선;
        var 노선_생성_응답 = 노선_생성_요청(노선1);

        // when
        var uri = 노선_생성_응답.header("Location");
        var 노선_삭제_응답 = 노선_삭제_요청(uri);

        // then
        노선_삭제_완료(노선_삭제_응답);
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 이름으로 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("중복 이름으로 지하철 노선 생성")
    @Test
    void createLineWithDuplicateName() {
        // given
        역_생성_요청(StationFixture.신논현역);
        역_생성_요청(StationFixture.강남역);

        var 노선1 = LineFixture.신분당선;
        노선_생성_요청(노선1);

        // when
        var 노선_생성_응답 = 노선_생성_요청(노선1);

        // then
        중복된_노선_생성_예외(노선_생성_응답);
    }

}
