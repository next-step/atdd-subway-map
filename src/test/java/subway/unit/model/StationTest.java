package subway.unit.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.unit.UnitTestFixture.강남역;
import static subway.unit.UnitTestFixture.역삼역;

class StationTest {

    @DisplayName("지하철역 생성 시 이전 지하철역 id 확인")
    @Test
    void getPreStationId() {
        assertThat(역삼역.getPreStationId()).isEqualTo(강남역.getId());
    }
}
