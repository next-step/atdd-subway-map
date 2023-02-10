package subway.Acceptance.line;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.Acceptance.AcceptanceTest;

import java.util.List;

import static subway.Acceptance.line.LineAcceptanceFixture.구간_등록_요청;
import static subway.Acceptance.line.LineAcceptanceFixture.*;
import static subway.Acceptance.line.LineAcceptanceStep.구간_등록_요청;
import static subway.Acceptance.line.LineAcceptanceStep.*;
import static subway.Acceptance.station.StationAcceptanceStep.지하철역_생성_요청;

@DisplayName("노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    void setStations() {
        지하철역_생성_요청("강남역");
        지하철역_생성_요청("역삼역");
        지하철역_생성_요청("선릉역");
        지하철역_생성_요청("삼성역");
        지하철역_생성_요청("선정릉");
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        // when
        var 노선_생성_응답 = 노선_생성_요청(이호선(강남역, 역삼역));

        // then
        노선_생성됨(노선_생성_응답);

        // then
        var 노선_목록_조회_응답 = 노선_목록_조회_요청();
        노선_목록_포함됨(노선_목록_조회_응답, List.of(노선_생성_응답));
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void showLines() {
        // given
        var 노선_생성_응답1 = 노선_생성_요청(이호선(강남역, 역삼역));
        var 노선_생성_응답2 = 노선_생성_요청(분당선(선릉역, 강남역));

        // when
        var 노선_목록_조회_응답 = 노선_목록_조회_요청();

        // then
        노선_목록_포함됨(노선_목록_조회_응답, List.of(노선_생성_응답1, 노선_생성_응답2));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void showLine() {
        // given
        var 노선_생성_응답 = 노선_생성_요청(이호선(강남역, 역삼역));

        // when
        var 노선_조회_응답 = 노선_조회_요청(노선_생성_응답);

        // then
        노선_조회됨(노선_조회_응답);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void modifyLine() {
        // given
        var 노선_생성_응답 = 노선_생성_요청(이호선(강남역, 역삼역));

        // when
        var 노선_수정_응답 = 노선_수정_요청(노선_생성_응답, 당당선_수정(당당선, 빨간색));

        // then
        노선_수정됨(노선_수정_응답);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        // given
        var 노선_생성_응답 = 노선_생성_요청(이호선(강남역, 역삼역));

        // when
        var 노선_삭제_응답 = 노선_삭제_요청(노선_생성_응답);

        // then
        노선_삭제됨(노선_삭제_응답);
    }

    /**
     * given 지하철 노선 생성
     * when 지하철 구간을 등록하면
     * then 지하철 구간이 등록된다.
     */
    @DisplayName("지하철 노선 구간 등록 성공")
    @Test
    void createLineSection() {
        // given
        var 노선_생성_응답 = 노선_생성_요청(이호선(강남역, 역삼역));

        // when
        var 구간_등록_응답 = 구간_등록_요청(이호선, 구간_등록_요청(역삼역, 선릉역));

        // then
        노선_구간_생성됨(구간_등록_응답);
    }

    /**
     * given 지하철 노선 생성
     * when 지하철 구간을 등록하면
     * then 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이 아니라면 실패한다.
     */
    @DisplayName("지하철 노선 구간 등록 실패 : 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.")
    @Test
    void createLineSection_fail1() {
        // given
        var 노선_생성_응답 = 노선_생성_요청(이호선(강남역, 역삼역));

        // when
        var 구간_등록_응답 = 구간_등록_요청(이호선, 구간_등록_요청(강남역, 선릉역));

        // then
        노선_구간_생성_실패됨(구간_등록_응답);
    }

    /**
     * given 지하철 노선 생성
     * when 지하철 구간을 등록하면
     * then 새로운 구간의 하행역이 해당 노선에 등록되어있는 역이라면 실패한다.
     */
    @DisplayName("지하철 노선 구간 등록 실패 : 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.")
    @Test
    void createLineSection_fail2() {
        // given
        var 노선_생성_요청 = 노선_생성_요청(이호선(강남역, 역삼역));

        // when
        var 구간_등록_응답 = 구간_등록_요청(이호선, 구간_등록_요청(역삼역, 강남역));

        // then
        노선_구간_생성_실패됨(구간_등록_응답);
    }

    /**
     * given 지하철 노선 등록
     * when 지하철 구간을 제거하면
     * then 지하철 구간이 삭제된다.
     */
    @DisplayName("지하철 노선 구간 삭제 성공")
    @Test
    void deleteLineSection() {
        // given
        var 노선_생성_응답 = 노선_생성_요청(이호선(강남역, 역삼역));
        var 구간_등록_응답 = 구간_등록_요청(이호선, 구간_등록_요청(역삼역, 선릉역));

        // when
        var 구간_삭제_응답 = 구간_삭제_요청(이호선, 선릉역);

        // then
        노선_구간_삭제됨(구간_삭제_응답);
    }

    /**
     * given 지하철 노선 등록
     * when 지하철 구간을 제거하면
     * then 지하철 노선에 등록된 역(하행 종점역)이 아니라면 실패한다.
     */
    @DisplayName("지하철 노선 구간 삭제 실패 : 지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다.")
    @Test
    void deleteLineSection_fail1() {
        // given
        var 노선_생성_응답 = 노선_생성_요청(이호선(강남역, 역삼역));
        var 구간_등록_응답 = 구간_등록_요청(이호선, 구간_등록_요청(역삼역, 선릉역));

        // when
        var 구간_삭제_응답 = 구간_삭제_요청(이호선, 역삼역);

        // then
        노선_구간_삭제_실패됨(구간_삭제_응답);
    }

    /**
     * given 지하철 노선 등록
     * when 지하철 구간을 제거하면
     * then 지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제가 실패한다.
     */
    @DisplayName("지하철 노선 구간 삭제 실패 : 지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.")
    @Test
    void deleteLineSection_fail2() {
        // given
        var 노선_생성_응답 = 노선_생성_요청(이호선(강남역, 역삼역));

        // when
        var 구간_삭제_응답 = 구간_삭제_요청(이호선, 역삼역);

        // then
        노선_구간_삭제_실패됨(구간_삭제_응답);
    }
}
