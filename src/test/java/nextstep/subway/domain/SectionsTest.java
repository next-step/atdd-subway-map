package nextstep.subway.domain;

import nextstep.subway.domain.exception.InvalidMatchEndStationException;
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
}