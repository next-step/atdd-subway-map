package subway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철 노선에 구간을 등록하면
     * Then 지하철 노선 조회 시, 역 목록에서 추가한 구간의 하행역을 찾을 수 있다.
     */
    @DisplayName("지하철 구간 등록")
    @Test
    void addSection() {
        // when

        // then
    }

    /**
     * When 지하철 노선의 하행 종점역이 아닌 역을 상행역으로 하는 구간을 등록하면
     * Then 지하철 구간 등록에 실패한다.
     */
    @DisplayName("새로운 구간의 상행역이 지하철 노선의 하행 종점역이 아닐 경우, 등록 불가")
    @Test
    void cannotAddSectionWhatIsUpStationNotEqualToLastDownStationInLine() {
        // when

        // then
    }

    /**
     * When 지하철 노선의 하행 종점역을 하행역으로 하는 구간을 등록하면
     * Then 지하철 구간 등록에 실패한다.
     */
    @DisplayName("새로운 구간의 하행역이 지하철 노선에 이미 등록되어있을 경우, 등록 불가")
    @Test
    void cannotAddSectionWhatDownStationIsAlreadyRegisteredInLIne() {
        // when
    
        // then
    }

    /**
     * Given 한 개의 구간이 존재하는 지하철 노선을 생성하고
     * Given 해당 노선의 구간을 추가한 뒤,
     * When 지하철 노선의 중간 역(추가한 구간의 상행역)을 제거하면
     * Then 지하철 구간 삭제에 실패한다.
     */
    @DisplayName("마지막 구간이 아닌 구간을 삭제할 경우, 삭제 불가")
    @Test
    void cannotDeleteSectionWhatIsNotLastSection() {
        // given
        
        // when
    
        // then
    }

    /**
     * Given 한 개의 구간이 존재하는 지하철 노선을 생성하고
     * When 지하철 노선의 하행 종점역을 제거하면
     * Thne 지하철 구간 삭제에 실패한다.
     */
    @DisplayName("지하철 노선의 구간이 1개일 경우, 구간 삭제 불가")
    @Test
    void cannotDeleteSectionWhenSectionCountInLineIsOne() {
        // given
        
        // when
    
        // then
    }

    /**
     * Given 한 개의 구강이 존재하는 지하철 노선을 생성하고
     * Given 해당 노선에 구간을 추가한 뒤,
     * When 지하철 노선의 하행 종점역(추가한 구간의 하행역)을 제거하면
     * Then 지하철 노선 조회 시, 역 목록에서 추가한 구간의 하행역을 찾을 수 없다.
     */
    @DisplayName("지하철 마지막 구간 제거")
    @Test
    void deleteLastSection() {
        // given

        // when

        // then
    }
}
