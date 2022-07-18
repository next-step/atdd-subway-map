package nextstep.subway.domain;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StationTest {

    @DisplayName("id 값이 같음을 확인할 수 있다")
    @Test
    void isLastDownStationTrue() {
        //given
        final var station = new Station(1L, "역삼역");
        //when
        boolean result = station.equalsId(1L);
        //then
        assertTrue(result);
    }


    @DisplayName("id 값이 다름을 확인할 수 있다.")
    @Test
    void isLastDownStationFalse() {
        //given
        final var station = new Station(1L, "역삼역");
        //when
        boolean result = station.equalsId(2L);
        //then
        assertFalse(result);
    }

}