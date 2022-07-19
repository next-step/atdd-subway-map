package nextstep.subway.domain;


import nextstep.subway.exception.DeleteStationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class SectionsTest {

    @DisplayName("구간 등록을 할 수 있다.")
    @Test
    void addSection() {
        //given
        final var firstStation = new Station(1L, "역삼역");
        final var secondStation = new Station(2L, "선릉역");
        final var thirdStation = new Station(4L, "삼성역");

        final var firstSection = new Section(firstStation, secondStation, 10);
        final var secondSection = new Section(secondStation, thirdStation, 3);

        //when
        Sections sections = new Sections();
        sections.add(firstSection);
        sections.add(secondSection);

        //then
        assertThat(sections.getSections()).containsExactly(firstSection, secondSection);
    }

    @DisplayName("구간 등록을 할 수 있다.")
    @Test
    void addSectionWhenOnlyOneStationExist() {
        //given
        final var upStation = new Station(1L, "역삼역");
        final var downStation1 = new Station(2L, "선릉역");
        final var downStation2 = new Station(4L, "삼성역");

        final var firstSection = new Section(upStation, downStation1, 10);
        firstSection.deleteDownStation();

        final var secondSection = new Section(upStation, downStation2, 3);
        //when
        Sections sections = new Sections();
        sections.add(firstSection);
        sections.add(secondSection);

        //then
        assertThat(sections.getSections()).containsExactly(firstSection);
        assertThat(sections.allStations()).containsExactly(upStation, downStation2);
    }

    @DisplayName("구간의 첫번째 역을 찾을 수 있다.")
    @Test
    void findStation() {
        //given
        final var firstStation = new Station(1L, "역삼역");
        final var secondStation = new Station(2L, "선릉역");
        final var thirdStation = new Station(4L, "삼성역");

        final var firstSection = new Section(firstStation, secondStation, 10);
        final var secondSection = new Section(secondStation, thirdStation, 3);
        //when
        Sections sections = new Sections();
        sections.add(firstSection);
        sections.add(secondSection);

        //then
        assertThat(sections.firstStation()).isEqualTo(firstStation);
    }

    @DisplayName("구간의 모든 역을 찾을 수 있다.")
    @Test
    void allStations() {
        //given
        final var firstStation = new Station(1L, "역삼역");
        final var secondStation = new Station(2L, "선릉역");
        final var thirdStation = new Station(4L, "삼성역");

        final var firstSection = new Section(firstStation, secondStation, 10);
        final var secondSection = new Section(secondStation, thirdStation, 3);
        //when
        Sections sections = new Sections();
        sections.add(firstSection);
        sections.add(secondSection);

        //then
        assertThat(sections.allStations()).containsExactly(firstStation, secondStation, thirdStation);
    }

    @DisplayName("구간에 역의 id를 통해 존재여부를 파악할 수 있다.")
    @Test
    void hasStation() {
        //given
        final var firstStation = new Station(1L, "역삼역");
        final var secondStation = new Station(2L, "선릉역");
        final var thirdStation = new Station(4L, "삼성역");

        final var firstSection = new Section(firstStation, secondStation, 10);
        final var secondSection = new Section(secondStation, thirdStation, 3);
        //when
        Sections sections = new Sections();
        sections.add(firstSection);
        sections.add(secondSection);

        //then
        assertAll(
                () -> assertThat(sections.hasStation(1L)).isTrue(),
                () -> assertThat(sections.hasStation(2L)).isTrue(),
                () -> assertThat(sections.hasStation(4L)).isTrue(),
                () -> assertThat(sections.hasStation(5L)).isFalse()
        );
    }

    @DisplayName("하행역 삭제를 통해 구간을 삭제할 수 있다.")
    @Test
    void deleteSection() {
        //given
        final var firstStation = new Station(1L, "역삼역");
        final var secondStation = new Station(2L, "선릉역");
        final var thirdStation = new Station(4L, "삼성역");

        final var firstSection = new Section(firstStation, secondStation, 10);
        final var secondSection = new Section(secondStation, thirdStation, 3);

        Sections sections = new Sections();
        sections.add(firstSection);
        sections.add(secondSection);
        //when
        sections.delete(4L);

        //then
        assertThat(sections.getSections()).hasSize(1);
    }

    @DisplayName("하행역 삭제 시 구간이 한개밖에 없다면 역만 삭제한다.")
    @Test
    void deleteSectionWhenOnlyOneSection() {
        //given
        final var upStation = new Station(1L, "역삼역");
        final var downStation = new Station(2L, "선릉역");

        final var section = new Section(upStation, downStation, 10);

        Sections sections = new Sections();
        sections.add(section);

        //when
        sections.delete(2L);

        //then
        assertAll(
                () -> assertThat(sections.getSections()).hasSize(1),
                () -> assertThat(sections.allStations()).containsExactly(upStation)
        );

    }

    @DisplayName("삭제 시 하행역이 아니면 삭제할 수 없다")
    @Test
    void cantDeleteNotDownStation() {
        //given
        final var firstStation = new Station(1L, "역삼역");
        final var secondStation = new Station(2L, "선릉역");
        final var thirdStation = new Station(4L, "삼성역");

        final var firstSection = new Section(firstStation, secondStation, 10);
        final var secondSection = new Section(secondStation, thirdStation, 3);

        Sections sections = new Sections();
        sections.add(firstSection);
        sections.add(secondSection);
        //when, then
        assertThatThrownBy(() -> sections.delete(2L))
                .isInstanceOf(DeleteStationException.class)
                .hasMessage("하행역만 삭제할 수 있습니다.");
    }


    @DisplayName("삭제 시 상행역과 하행역만이 존재하면 삭제할 수 없다")
    @Test
    void cantDeleteOnlyOneStationInSections() {
        //given
        final var upStation = new Station(1L, "역삼역");
        final var downStation = new Station(2L, "선릉역");

        final var section = new Section(upStation, downStation, 10);
        section.deleteDownStation();

        Sections sections = new Sections();
        sections.add(section);
        //when, then
        assertThatThrownBy(() -> sections.delete(1L))
                .isInstanceOf(DeleteStationException.class)
                .hasMessage("상행역과 하행역만 존재하기 때문에 삭제할 수 없습니다.");
    }



}