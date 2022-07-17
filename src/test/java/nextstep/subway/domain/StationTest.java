package nextstep.subway.domain;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StationTest {

    @DisplayName("id 값 전달을 통해 하행 마지막역임을 확인할 수 있다")
    @Test
    void isLastDownStationTrue() {
        //given
        final var station = new Station(1L, "역삼역");
        //when
        boolean result = station.isLastDownStation(1L);
        //then
        assertTrue(result);
    }


    @DisplayName("id 값 전달을 통해 하행 마지막역이 아님을 확인할 수 있다")
    @Test
    void isLastDownStationFalse() {
        //given
        final var station = new Station(1L, "역삼역");
        //when
        boolean result = station.isLastDownStation(2L);
        //then
        assertFalse(result);
    }

}