package subway.unit.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.model.Stations;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.unit.UnitTestFixture.*;

class StationsTest {

    @DisplayName("지하철 상행 / 하행 확인")
    @Test
    void getUpStation() {
        // given
        Stations stations = 강남역_역삼역;

        // when
        Long upStationId = stations.getUpStationId();
        Long downStationId = stations.getDownStationId();

        // then
        assertThat(upStationId).isEqualTo(강남역.getId());
        assertThat(downStationId).isEqualTo(역삼역.getId());
    }

}
