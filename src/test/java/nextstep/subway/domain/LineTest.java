package nextstep.subway.domain;

import nextstep.subway.exception.NotLastStationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class LineTest {

    @DisplayName("LineUpdateRequest 를 통해 line 을 바꿀 수 있다.")
    @Test
    void changeLine() {
        //given
        final var stations = List.of(new Station("역삼역"));
        final var line = new Line("name", "color", stations, 10);

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
        final var stations = List.of(
                new Station(3L, "강남역"),
                new Station(1L, "선릉역"),
                new Station(2L, "역삼역")
        );
        final var line = new Line("2호선", "bg-green-600", stations, 20);

        //when
        final var station = line.lastStation();

        //then
        assertThat(station.getName()).isEqualTo("강남역");
    }

    @DisplayName("역의 id 를 통해 라인에 포함여부를 파악할 수 있다")
    @Test
    void hasStation() {
        //given
        final var stations = List.of(
                new Station(3L, "강남역"),
                new Station(1L, "선릉역"),
                new Station(2L, "역삼역")
        );
        final var line = new Line("2호선", "bg-green-600", stations, 20);

        //when, then
        assertAll(
                () -> assertTrue(line.hasStation(1L)),
                () -> assertFalse(line.hasStation(4L))
        );
    }

    @DisplayName("역의 id 를 받아 제거할 수 있다.")
    @Test
    void deleteStation() {
        //given
        final var stations = List.of(
                new Station(3L, "강남역"),
                new Station(1L, "선릉역"),
                new Station(2L, "역삼역")
        );
        final var line = new Line("2호선", "bg-green-600", stations, 20);

        //when
        line.deleteStation(3L);
        //then
        assertThat(line.getStations()).hasSize(2);
    }

    @DisplayName("역의 id 가 하행역이 아니면 삭제 시 익셉션이 발생한다.")
    @Test
    void deleteStationNotDownStationException() {
        //given
        final var stations = List.of(
                new Station(3L, "강남역"),
                new Station(1L, "선릉역"),
                new Station(2L, "역삼역")
        );
        final var line = new Line("2호선", "bg-green-600", stations, 20);

        //when, then
        assertThatThrownBy(() -> line.deleteStation(2L))
                .isInstanceOf(NotLastStationException.class)
                .hasMessage("하행역만 삭제할 수 있습니다.");
    }

}