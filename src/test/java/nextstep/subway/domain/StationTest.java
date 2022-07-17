package nextstep.subway.domain;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StationTest {

    @DisplayName("역의 id 가 같다면 결괏값은 true 이다.")
    @Test
    void stationEqualsIdTrue() {
        //given
        final var station = new Station(1L, "역삼역");
        //when
        boolean result = station.equalsId(1L);
        //then
        assertTrue(result);
    }


    @DisplayName("역의 id 가 같다면 결괏값은 true 이다.")
    @Test
    void stationEqualsIdFalse() {
        //given
        final var station = new Station(1L, "역삼역");
        //when
        boolean result = station.equalsId(2L);
        //then
        assertFalse(result);
    }

}