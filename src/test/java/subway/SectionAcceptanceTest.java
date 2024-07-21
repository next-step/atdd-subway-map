package subway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.test.AcceptanceTestBase;

public class SectionAcceptanceTest extends AcceptanceTestBase {

    //given: 지하철 노선이 등록되어 있다
    //when: 노선의 하행 종점역을 상행역으로 하는 구간을 등록한다
    //then: 새로운 구간이 등록된다
    //then: 노선의 하행 종점역이 변경된다
    @DisplayName("지하철 노선 구간을 등록한다")
    @Test
    void createSection() {
    }

    //given: 지하철 노선이 등록되어 있다
    //when 노선의 하행 종점역이 아닌 역을 상행역으로 하는 구간을 등록한다
    //then 구간 등록에 실패한다
    @DisplayName("구간의 상행역이 노선의 하행 종점역이 아닌 경우 구간을 등록할 수 없다")
    @Test
    void failToCreateSection() {
    }

    //given: 지하철 노선이 등록되어 있다
    //when: 새로운 구간을 등록할 때, 이미 등록된 역을 하행역으로 사용한다
    //then 구간 등록에 실패한다
    @DisplayName("구간의 하행역이 이미 노선에 등록된 경우 구간을 등록할 수 없다")
    @Test
    void failToCreateSection2() {
    }

    //given: 구간이 2개 이상인 지하철 노선이 등록되어 있다
    //when: 지하철 노선의 하행 종점역을 제거한다
    //then: 구간 제거에 성공한다
    //then: 노선의 하행 종점역이 변경된다.
    @DisplayName("지하철 구간 제거에 성공한다")
    @Test
    void deleteSection() {
    }

    //given: 구간이 2개 이상인 지하철 노선이 등록되어 있다
    //when: 지하철 노선의 히헹 종점역이 아닌 역을 제거한다
    //then: 구간 제거에 실패한다
    @DisplayName("노선의 하행 종점역 이외의 역은 삭제할 수 없다")
    @Test
    void failToDeleteSection() {
    }

    //given: 구간이 1개인 지하철 노선이 등록되어있다
    //when: 지하철 노선의 하행 종점역을 제거한다
    //then: 구간 제거에 실패한다
    @DisplayName("노선의 구간이 1개인 경우 역을 삭제할 수 없다")
    @Test
    void failToDeleteSection2() {
    }
}
