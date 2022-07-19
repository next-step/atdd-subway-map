package nextstep.subway.domain;

import nextstep.subway.exception.DeleteStationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class LineTest {

    @DisplayName("LineUpdateRequest 를 통해 line 을 바꿀 수 있다.")
    @Test
    void changeLine() {
        //given
        final var line = new Line("name", "color", new Station(1L, "역삼역"), new Station(2L, "강남역"), 1);

        //when
        final var changedLine = line.changeBy("new name", "new color");

        //then
        assertAll(
                () -> assertThat(changedLine.getName()).isEqualTo("new name"),
                () -> assertThat(changedLine.getColor()).isEqualTo("new color")
        );
    }

    @DisplayName("노선의 마지막 역을 찾을 수 있다.")
    @Test
    void lastStationAtLine() {
        //given
        final var line = new Line("2호선", "bg-green-600", new Station(1L, "역삼역"), new Station(2L, "강남역"), 1);

        //when
        final var station = line.lastStation();

        //then
        assertThat(station.getName()).isEqualTo("강남역");
    }

    @DisplayName("역의 id 를 통해 라인에 포함여부를 파악할 수 있다")
    @Test
    void hasStation() {
        //given
        final var line = new Line("2호선", "bg-green-600", new Station(1L, "역삼역"), new Station(2L, "강남역"), 1);

        //when, then
        assertAll(
                () -> assertTrue(line.hasStation(1L)),
                () -> assertFalse(line.hasStation(3L))
        );
    }

    @DisplayName("역의 id 를 받아 제거할 수 있다.")
    @Test
    void deleteStation() {
        //given
        final var line = new Line("2호선", "bg-green-600", new Station(1L, "역삼역"), new Station(2L, "강남역"), 1);

        //when
        line.deleteSection(2L);

        //then
        assertThat(line.allStations().stream().map(Station::getId)).containsExactly(1L);
    }

    @DisplayName("역의 id 가 하행역이 아니면 삭제할 수 없다.")
    @Test
    void deleteStationNotDownStationException() {
        //given
        final var line = new Line("2호선", "bg-green-600", new Station(1L, "역삼역"), new Station(2L, "강남역"), 1);

        //when, then
        assertThatThrownBy(() -> line.deleteSection(1L))
                .isInstanceOf(DeleteStationException.class)
                .hasMessage("하행역만 삭제할 수 있습니다.");
    }

    @DisplayName("상행역과 하행역만이 존재하는 경우 삭제할 수 없다.")
    @Test
    void deleteStationOnlyOneStationException() {
        //given
        final var line = new Line("2호선", "bg-green-600", new Station(1L, "역삼역"), new Station(2L, "강남역"), 1);
        line.deleteSection(2L);

        //when, then
        assertThatThrownBy(() -> line.deleteSection(1L))
                .isInstanceOf(DeleteStationException.class)
                .hasMessage("상행역과 하행역만 존재하기 때문에 삭제할 수 없습니다.");
    }

}