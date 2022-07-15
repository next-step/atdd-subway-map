package nextstep.subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StationTest {

    @Test
    @DisplayName("이전의 하행역과 새로운 상행역이 같지 않으면 에러")
    void beforeStationEqualNewUpStation() {
        Station downStation = new Station(1L, "강남역");

        assertThatThrownBy(() -> downStation.isSameStationId(2L))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("하행역과 새로운 상행역이 맞지 않습니다.");
    }

}