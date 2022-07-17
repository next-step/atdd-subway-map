package nextstep.subway.domain;

import nextstep.subway.exception.InvalidDistanceValueException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DistanceTest {

    @Test
    @DisplayName("유효하지 않은 거리 값 - 0보다 작은 수")
    void invalidDistance() {
        // given
        int upStationDistance = 1;
        int downStationDistance = -1;

        // when
        Executable executable = () -> new Distance(upStationDistance, downStationDistance);

        // then
        assertThrows(InvalidDistanceValueException.class, executable);
    }

    @Test
    @DisplayName("유효한 거리 값 - 0보다 같거나 큰 수")
    void validDistance() {
        // given
        int upStationDistance = 0;
        int downStationDistance = 10;

        // when
        Distance distance = new Distance(upStationDistance, downStationDistance);

        // then
        assertThat(distance.getUpStationDistance()).isEqualTo(upStationDistance);
        assertThat(distance.getDownStationDistance()).isEqualTo(downStationDistance);
    }

}