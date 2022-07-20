package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class SectionTest {

    @DisplayName("역의 id 를 통해 구간 포함 여부를 파악할 수 있다.")
    @Test
    void hasStation() {
        //given
        final var upStation = new Station(1L, "역삼역");
        final var downStation = new Station(2L, "선릉역");
        final var section = new Section(upStation, downStation, 10);
        //when, then
        assertAll(
                () -> assertTrue(section.hasStation(new Station(1L, "역삼역"))),
                () -> assertFalse(section.hasStation(new Station(3L, "서초역")))
        );
    }

    @DisplayName("구간에서 하행역을 추가할 수 있다.")
    @Test
    void addDownStation() {
        //given
        final var upStation = new Station(1L, "역삼역");
        final var section = new Section(upStation, null, 10);
        //when
        final var downStation = new Station(2L, "선릉역");
        section.addDownStation(downStation);
        // then
        assertThat(section.getDownStation()).isNotNull();
    }


}