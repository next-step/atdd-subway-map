package nextstep.subway.acceptance;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends BaseAcceptanceTest{

    /**
     * Given    하행 종점역에 등록할 새로운 구간을 생성하고
     * When     새로운 구간을 지하철 노선의 하행 종점역에 등록하면
     * Then     지하철 노선의 구간 목록에서 생성된 구간을 찾을 수 있다.
     * Then     지하철 노선의 하행 종점역이 변경된다.
     */
    @Test
    @DisplayName("지하철 노선의 하행 종점역에 구간을 등록한다.")
    void registerSectionTest() {

    }

    /**
     * Given    새로운 구간을 생성하고
     * When     새로운 구간의 상행역이 해당 노선에 등록된 하행 종점역이 아니면
     * Then     에러처리한다.
     */
    @Test
    @DisplayName("지하철 노선의 하행 종점역이 아닌 역에 구간을 등록한다.")
    void registerIllegalUpStationSectionTest() {

    }

    /**
     * Given    새로운 구간을 생성하고
     * When     새로운 구간의 하행역이 해당 노선에 등록되어있는 역이면
     * Then     에러처리한다.
     */
    @Test
    @DisplayName("지하철 노선의 하행 종점역이 아닌 역에 구간을 등록한다.")
    void registerIllegalDownStationSectionTest() {

    }

    /**
     * Given    지하철 노선의 하행 종점역에 새로운 구간을 등록하고
     * When     해당 노선의 하행 종점역의 구간을 제거하면
     * Then     지하철 노선의 구간 목록에서 제거한 구간을 찾을 수 없다.
     * Then     지하철 노선의 하행 종점역이 변경된다.
     */
    @Test
    @DisplayName("지하철 노선의 하행 종점역의 구간을 제거한다.")
    void removeSectionTest() {

    }

    /**
     * Given    지하철 노선의 상행 종점역에 새로운 구간을 등록하고
     * When     삭제할 구간이 하행 종점역이 아니면
     * Then     에러처리한다.
     */
    @Test
    @DisplayName("지하철 노선의 하행 종점역이 아닌 역의 구간을 제거한다.")
    void removeIllegalDownStationSectionTest() {

    }

    /**
     * When     삭제할 지하철 노선의 구간이 1개이면
     * Then     에러처리한다.
     */
    @Test
    @DisplayName("지하철 노선의 구간이 1개일 때 구간을 제거한다.")
    void remainAtLeastOneSectionTest() {

    }

}
