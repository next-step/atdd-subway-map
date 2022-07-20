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
        boolean result = station.equals(new Station(1L, "역삼역"));
        //then
        assertTrue(result);
    }


    @DisplayName("id 값이 다름을 확인할 수 있다.")
    @Test
    void isLastDownStationFalse() {
        //given
        final var station = new Station(1L, "역삼역");
        //when
        boolean result = station.equals(new Station(2L, "강남역"));
        //then
        assertFalse(result);
    }

}