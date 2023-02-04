package subway.section;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 구간 관리 기능")
class SectionAcceptanceTest {

    /**
     * Given 지하철역, 지하철 노선을 생성요청하고
     */
    @BeforeEach
    void setUp() {
    }

    /**
     * When 지하철 노선에 새로운 구간을 등록 요청하면
     * Then 지하철 노선에 새로운 구간이 추가된다
     */
    @DisplayName("지하철 노선에 새로운 구간을 등록한다")
    @Test
    void addSection() {
    }

    /**
     * Given 지하철 노선에 새로운 구간을 등록 요청하고
     * When 지하철 노선에 마지막 구간을 제거 요청하면
     * Then 지하철 노선에 마지막 구간이 제거된다
     */
    @DisplayName("지하철 노선의 구간을 제거한다")
    @Test
    void removeSection() {
    }
}
