package subway.section;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.AcceptanceTest;
import subway.station.dto.StationResponse;

import static org.assertj.core.api.Assertions.*;
import static subway.line.LineTestUtils.*;
import static subway.section.SectionUtils.*;
import static subway.station.StationTestUtils.*;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {


    /**
     * Given 3개의 자하철 역을 생성
     * Given 지하철 노선을 생성 후
     * When 지하철 구간을 등록하면
     * Then 지하철 노선 조회 시, 전체 역 개수는 3개다.
     */
    @DisplayName("구간을 등록한다.")
    @Test
    void enrollSection() {
        // given
        String 상행종착역_URL = 지하철역_생성(강남역_정보);
        String 하행종착역_URL = 지하철역_생성(역삼역_정보);
        String 새로운_하행역_URL = 지하철역_생성(삼성역_정보);
        String 이호선_URL = 지하철_노선_생성(이호선_생성_요청, 상행종착역_URL, 하행종착역_URL);

        // when
        지하철_구간_등록(이호선_URL, 구간_등록_요청, 하행종착역_URL, 새로운_하행역_URL);

        // then
        지하철_구간_등록_검증(이호선_URL);
    }

    /**
     * Given 3개의 자하철 역을 생성
     * Given 지하철 노선을 생성 후
     * When 지하철 구간을 잘못 등록하면
     * - 새로운 구간의 상행 역이 해당 노선에 등록되어있는 하행 종적역이 아님.
     * Then 요청은 에러 처리되고
     * Then 지하철 노선 조회 시, 전체 역 개수는 2개다.
     */
    @DisplayName("구간 등록 에러, 기존 하행종착역-새 구간 상행역 불일치")
    @Test
    void enrollSectionErrorByInconsistency() {
        // given
        String 상행종착역_URL = 지하철역_생성(강남역_정보);
        String 하행종착역_URL = 지하철역_생성(역삼역_정보);
        String 새로운_하행역_URL = 지하철역_생성(삼성역_정보);
        String 이호선_URL = 지하철_노선_생성(이호선_생성_요청, 상행종착역_URL, 하행종착역_URL);

        // when
        지하철_구간_등록_실패(이호선_URL, 구간_등록_요청, 새로운_하행역_URL, 새로운_하행역_URL);

        // then
        지하철_구간_미등록_검증(이호선_URL);
    }

    /**
     * Given 3개의 지하철 역을 생성
     * Given 지하철 노선을 생성 후
     * When 지하철 구간을 잘못 등록
     * - 새로운 구간의 하행역이 해당 노선에 등록되어있는 역임.
     * Then 요청은 에러 처리된다.
     * Then 지하철 노선 조회 시, 전체 역 개수는 2개다.
     */
    @DisplayName("구간 등록 에러, 새구간 하행역-기존 구간 내 존재")
    @Test
    void enrollSectionErrorByNewDownStationExists() {
        // given
        String 상행종착역_URL = 지하철역_생성(강남역_정보);
        String 하행종착역_URL = 지하철역_생성(역삼역_정보);
        String 이호선_URL = 지하철_노선_생성(이호선_생성_요청, 상행종착역_URL, 하행종착역_URL);

        // when
        지하철_구간_등록_실패(이호선_URL, 구간_등록_요청, 하행종착역_URL, 상행종착역_URL);
        // then
        지하철_구간_미등록_검증(이호선_URL);
    }


    /**
     * Given 3개의 자하철 역 생성
     * Given 지하철 노선을 생성
     * Given 1개의 지하철 구간을 추가 후
     * When 지하철 구간을 삭제하면
     * Then 지하철 노선 조회 시, 전체 역 개수는 2개다.
     */
    @DisplayName("구간을 제거한다.")
    @Test
    void removeSection() {
        // given
        String 상행종착역_URL = 지하철역_생성(강남역_정보);
        String 하행종착역_URL = 지하철역_생성(역삼역_정보);
        String 새로운_하행역_URL = 지하철역_생성(삼성역_정보);
        String 이호선_URL = 지하철_노선_생성(이호선_생성_요청, 상행종착역_URL, 하행종착역_URL);
        지하철_구간_등록(이호선_URL, 구간_등록_요청, 하행종착역_URL, 새로운_하행역_URL);

        // when
        지하철_구간_삭제(이호선_URL + "/sections?stationId=" + 지하철_아이디_획득(새로운_하행역_URL));

        // then
        노선의역_개수_검증(이호선_URL, 2);
    }

    /**
     * Given 3개의 자하철 역 생성
     * Given 지하철 노선을 생성 후
     * When 지하철 구간을 잘못 삭제하면
     * - 마지막 구간 아닌 구간 제거
     * Then 요청은 에러 처리된다.
     * Then 지하철 노선 조회 시, 전체 역 개수는 3개다.
     */
    @DisplayName("구간을 제거 에러, 마지막 구간 아닌 구간 제거")
    @Test
    void removeSectionErrorByRemovingNonLastSection() {
        // given
        String 상행종착역_URL = 지하철역_생성(강남역_정보);
        String 하행종착역_URL = 지하철역_생성(역삼역_정보);
        String 새로운_하행역_URL = 지하철역_생성(삼성역_정보);
        String 이호선_URL = 지하철_노선_생성(이호선_생성_요청, 상행종착역_URL, 하행종착역_URL);
        지하철_구간_등록(이호선_URL, 구간_등록_요청, 하행종착역_URL, 새로운_하행역_URL);

        // when
        지하철_구간_삭제_실패(이호선_URL + "/sections?stationId=" + 지하철_아이디_획득(상행종착역_URL));

        // then
        노선의역_개수_검증(이호선_URL, 3);
    }

    /**
     * Given 3개의 자하철 역 생성
     * Given 지하철 노선을 생성 후
     * When 지하철 구간을 잘못 삭제하면
     * - 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우)
     * Then 요청은 에러 처리된다.
     * Then 지하철 노선 조회 시, 전체 역 개수는 3개다.
     */
    @DisplayName("구간을 제거 에러, 상행 종점역과 하행 종점역만 있는 경우")
    @Test
    void removeSectionErrorByOnlyOneSectionLeft() {
        // given
        String 상행종착역_URL = 지하철역_생성(강남역_정보);
        String 하행종착역_URL = 지하철역_생성(역삼역_정보);
        String 이호선_URL = 지하철_노선_생성(이호선_생성_요청, 상행종착역_URL, 하행종착역_URL);

        // when
        지하철_구간_삭제_실패(이호선_URL + "/sections?stationId=" + 지하철_아이디_획득(하행종착역_URL));

        // then
        노선의역_개수_검증(이호선_URL, 2);
    }

    private void 지하철_구간_등록_검증(String 노선_url) {
        assertThat(지하철_노선_조회(노선_url).jsonPath().getList("stations", StationResponse.class))
                .hasSize(3);
    }

    private static void 지하철_구간_미등록_검증(String 노선_url) {
        assertThat(지하철_노선_조회(노선_url).response().jsonPath().getList("stations", StationResponse.class))
                .hasSize(2);
    }

    private void 노선의역_개수_검증(String 노선_url, int 역_개수) {
        assertThat(지하철_노선_조회(노선_url).jsonPath().getList("stations", StationResponse.class))
                .hasSize(역_개수);
    }
}
