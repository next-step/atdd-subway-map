package subway;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {


    /**
     * Given 3개의 자하철 역을 생성하고
     * Given 지하철 노선을 생성하고
     * When 지하철 구간을 등록하면
     * Then 지하철 노선 조회 시 하행 종착역이 등록한 구간의 하행 종착역이고 총 길이가 바뀌어야한다.
     */
    @DisplayName("구간을 등록한다.")
    @Test
    void enrollSection() {

    }

    /**
     * Given 3개의 자하철 역을 생성하고
     * Given 지하철 노선을 생성하고
     * When 지하철 구간을 잘못 등록하면
     * - 새로운 구간의 상행 역이 해당 노선에 등록되어있는 하행 종적역이 아님.
     * Then 요청은 에러 처리된다.
     */
    @DisplayName("구간 등록 에러, 기존 하행종착역-새 구간 상행역 불일치")
    @Test
    void enrollSectionErrorByInconsistency() {

    }

    /**
     * Given 3개의 지하철 역을 생성하고
     * Given 지하철 노선을 생성하고
     * When 지하철 구간을 잘못 등록하면
     * - 새로운 구간의 하행역이 해당 노선에 등록되어있는 역임.
     * Then 요청은 에러 처리된다.
     */
    @DisplayName("구간 등록 에러, 새구간 하행역-기존 구간 내 존재")
    @Test
    void enrollSectionErrorByNewDownStationExists() {

    }


    /**
     * Given 3개의 자하철 역 생성 후
     * Given 지하철 노선을 생성하고
     * When 지하철 구간을 삭제하면
     * Then 지하철 노선 조회 시 하행 종착역과 총 길이가 바뀌어야한다.
     */
    @DisplayName("구간을 제거한다.")
    @Test
    void removeSection() {

    }

    /**
     * Given 3개의 자하철 역 생성 후
     * Given 지하철 노선을 생성하고
     * When 지하철 구간을 잘못 삭제하면
     * - 마지막 구간 아닌 구간 제거
     * Then 요청은 에러 처리된다.
     */
    @DisplayName("구간을 제거 에러, 마지막 구간 아닌 구간 제거")
    @Test
    void removeSectionErrorByRemovingNonLastSection() {

    }

    /**
     * Given 3개의 자하철 역 생성 후
     * Given 지하철 노선을 생성하고
     * When 지하철 구간을 잘못 삭제하면
     * - 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우)
     * Then 요청은 에러 처리된다.
     */
    @DisplayName("구간을 제거 에러, 마지막 구간 아닌 구간 제거")
    @Test
    void removeSectionErrorByOnlyOneSectionLeft() {

    }
}
