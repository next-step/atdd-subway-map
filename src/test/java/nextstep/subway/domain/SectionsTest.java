package nextstep.subway.domain;

import nextstep.subway.domain.exception.InvalidMatchEndStationException;
import nextstep.subway.domain.exception.SectionDeleteException;
import nextstep.subway.domain.exception.StationAlreadyExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionsTest {

    @Test
    @DisplayName("해당 구간에 해당되는 지하철 역들을 모두 제공한다.")
    void getStations() {
        // given
        Section section1 = Section.create(StationTest.GANGNAM_STATION, StationTest.YEOKSAM_STATION, 10);
        Section section2 = Section.create(StationTest.YEOKSAM_STATION, StationTest.SEOLLEUNG_STATION, 5);
        Sections sections = Sections.create();

        // when
        sections.add(section1);
        sections.add(section2);

        // then
        assertThat(sections.stations().size()).isEqualTo(3);
        assertThat(sections.stations()).containsOnly(StationTest.GANGNAM_STATION,
                                                        StationTest.YEOKSAM_STATION,
                                                        StationTest.SEOLLEUNG_STATION);
    }

    @Test
    @DisplayName("추가하려는 구간의 상행역이 해당 노선의 하행 종점역과 같지 않으면 예외를 반환한다.")
    void invalid_new_section_wrong_upStation() {
        // given
        Sections sections = Sections.create();
        Section section = Section.create(StationTest.GANGNAM_STATION, StationTest.YEOKSAM_STATION, 10);
        sections.add(section);

        // when
        Section newSection = Section.create(StationTest.GANGNAM_STATION, StationTest.SEOLLEUNG_STATION, 5);

        // then
        assertThatThrownBy(() -> sections.add(newSection))
                .isInstanceOf(InvalidMatchEndStationException.class);
    }

    @Test
    @DisplayName("추가하려는 구간의 하행역이 해당 노선의 지하철역 일 경우 예외를 반환한다.")
    void invalid_new_section_wrong_downStation() {
        // given
        Sections sections = Sections.create();
        Section section = Section.create(StationTest.GANGNAM_STATION, StationTest.YEOKSAM_STATION, 10);
        sections.add(section);

        // when
        Section newSection = Section.create(StationTest.YEOKSAM_STATION, StationTest.GANGNAM_STATION, 5);

        // then
        assertThatThrownBy(() -> sections.add(newSection))
                .isInstanceOf(StationAlreadyExistsException.class);
    }

    @Test
    @DisplayName("구간이 추가되면 새로운 구간의 하행역이 포함된다.")
    void new_section() {
        // given
        Sections sections = Sections.create();
        Section section = Section.create(StationTest.GANGNAM_STATION, StationTest.YEOKSAM_STATION, 10);
        sections.add(section);

        // when
        Section newSection = Section.create(StationTest.YEOKSAM_STATION, StationTest.SEOLLEUNG_STATION, 5);
        sections.add(newSection);

        // then
        assertThat(sections.stations()).containsOnly(StationTest.GANGNAM_STATION,
                StationTest.YEOKSAM_STATION,
                StationTest.SEOLLEUNG_STATION);
    }

    @Test
    @DisplayName("Sections 가 하나의 구간만 가지고 있을 때 삭제 시 예외를 반환한다.")
    void invalid_delete_only_one_section() {
        // given
        Sections sections = Sections.create();
        Section section = Section.create(StationTest.GANGNAM_STATION, StationTest.YEOKSAM_STATION, 10);
        sections.add(section);

        // then
        assertThatThrownBy(() -> sections.delete(StationTest.YEOKSAM_STATION))
                .isInstanceOf(SectionDeleteException.class)
                .hasMessage("구간이 하나만 존재합니다.");
    }

    @Test
    @DisplayName("삭제요청한 구간이 마지막 구간이 아니면 예외를 반환한다.")
    void invalid_delete_not_last_section() {
        // given
        Sections sections = Sections.create();
        Section section = Section.create(StationTest.GANGNAM_STATION, StationTest.YEOKSAM_STATION, 10);
        sections.add(section);
        Section newSection = Section.create(StationTest.YEOKSAM_STATION, StationTest.SEOLLEUNG_STATION, 5);
        sections.add(newSection);

        // then
        assertThatThrownBy(() -> sections.delete(StationTest.YEOKSAM_STATION))
                .isInstanceOf(SectionDeleteException.class);
    }

    @Test
    @DisplayName("구간을 삭제하면 하행종점역이 사라진다.")
    void delete_section() {
        // given
        Sections sections = Sections.create();
        Section section = Section.create(StationTest.GANGNAM_STATION, StationTest.YEOKSAM_STATION, 10);
        sections.add(section);
        Section newSection = Section.create(StationTest.YEOKSAM_STATION, StationTest.SEOLLEUNG_STATION, 5);
        sections.add(newSection);

        // when
        sections.delete(StationTest.SEOLLEUNG_STATION);

        // then
        assertThat(sections.stations()).containsOnly(StationTest.GANGNAM_STATION, StationTest.YEOKSAM_STATION);
    }
}