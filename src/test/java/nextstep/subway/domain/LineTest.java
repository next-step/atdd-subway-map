package nextstep.subway.domain;

import nextstep.subway.exception.AlreadyExistStationException;
import nextstep.subway.exception.DeleteStationException;
import nextstep.subway.exception.SectionStationMismatchException;
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

    @DisplayName("역의 id 를 통해 라인에 포함여부를 파악할 수 있다.")
    @Test
    void hasStation() {
        //given
        final var line = new Line("2호선", "bg-green-600", new Station(1L, "역삼역"), new Station(2L, "강남역"), 1);

        //when, then
        assertAll(
                () -> assertTrue(line.hasStation(new Station(1L, "역삼역"))),
                () -> assertFalse(line.hasStation(new Station(3L, "서초역")))
        );
    }

    @DisplayName("구간을 추가하고 모든 역 확인을 통해 추가됨을 확인할 수 있다.")
    @Test
    void addSection() {
        //given
        final var firstStation = new Station(1L, "역삼역");
        final var secondStation = new Station(2L, "강남역");
        final var line = new Line("2호선", "bg-green-600", firstStation, secondStation, 1);

        final var thirdStation = new Station(3L, "서초역");
        //when
        Line sectionAddedLine = line.addSection(secondStation, thirdStation, 3);

        //then
        assertThat(sectionAddedLine.allStations()).containsExactly(firstStation, secondStation, thirdStation);

    }

    @DisplayName("구간을 추가 시 이미 존재하는 역이면 에러가 발생한다.")
    @Test
    void alreadyExistStationException() {
        //given
        final var firstStation = new Station(1L, "역삼역");
        final var secondStation = new Station(2L, "강남역");
        final var line = new Line("2호선", "bg-green-600", firstStation, secondStation, 1);

        final var duplicateStation = new Station(2L, "강남역");

        //when, then
        assertThatThrownBy(() -> line.addSection(secondStation, duplicateStation, 3))
                .isInstanceOf(AlreadyExistStationException.class)
                .hasMessage("이미 존재하는 역입니다.");

    }

    @DisplayName("구간을 추가 시 노선의 하행역과 구간의 상행역이 일치하지 않으면 에러가 발생한다.")
    @Test
    void sectionStationMismatchException() {
        //given
        final var firstStation = new Station(1L, "역삼역");
        final var secondStation = new Station(2L, "강남역");
        final var line = new Line("2호선", "bg-green-600", firstStation, secondStation, 1);

        final var thirdStation = new Station(3L, "강남역");
        final var fourthStation = new Station(4L, "서초역");

        //when, then
        assertThatThrownBy(() -> line.addSection(fourthStation, fourthStation, 3))
                .isInstanceOf(SectionStationMismatchException.class)
                .hasMessage("노선의 하행 마지막역과 추가되는 구간의 상행역이 달라 추가될 수 없습니다. 하행 마지막 역 : 2, 구간 상행역 : 4");

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