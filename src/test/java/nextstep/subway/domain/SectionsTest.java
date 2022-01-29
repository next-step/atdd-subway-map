package nextstep.subway.domain;

import nextstep.subway.exception.SectionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class SectionsTest extends SectionFixData {

    @DisplayName("구간 등록 시에 상행역이 아닌 하행역 등록은 예외")
    @Test
    void matchDownStationException() {
        // given
        Station station1 = createStation(FIRST_STATION_NAME);
        Station station2 = createStation(SECOND_STATION_NAME);
        Station station3 = createStation(THIRD_STATION_NAME);
        Station station4 = createStation(FOURTH_STATION_NAME);

        Sections sections = createSections(createSection(station1, station2), createSection(station2, station3));
        Section section = createSection(station4, station4);

        // when, then
        assertThatThrownBy(() -> sections.validationSectionStation(section.getUpStation(), section.getDownStation()))
                .isInstanceOf(SectionException.class)
                .hasMessage("하행역만 상행역으로 등록될 수 있습니다.");
    }

    @DisplayName("이미 구간에 등록된 역은 하행역 등록 시 예외")
    @Test
    void matchAllStationException() {
        // given
        Station station1 = createStation(FIRST_STATION_NAME);
        Station station2 = createStation(SECOND_STATION_NAME);
        Station station3 = createStation(THIRD_STATION_NAME);

        Sections sections = createSections(createSection(station1, station2), createSection(station2, station3));
        Section section = createSection(station3, station1);

        // when, then
        assertThatThrownBy(() -> sections.validationSectionStation(section.getUpStation(), section.getDownStation()))
                .isInstanceOf(SectionException.class)
                .hasMessage("이미 구간에 등록되어 있습니다.");
    }

    @DisplayName("마지막 구간 하행역 제거")
    @Test
    void lastSectionDelete() {
        // given
        Station station1 = createStation(FIRST_STATION_NAME);
        Station station2 = createStation(SECOND_STATION_NAME);
        Station station3 = createStation(THIRD_STATION_NAME);

        Sections sections = createSections(createSection(station1, station2)
                                            , createSection(station2, station3));

        // when
        sections.deleteStation(station3);

        // then
        assertThat(sections.getStations().size()).isEqualTo(2);
    }

    @DisplayName("마지막 구간 하행역이 아니면 제거 요청 시 예외")
    @Test
    void notLastSectionDeleteException() {
        // given
        Station station1 = createStation(FIRST_STATION_NAME);
        Station station2 = createStation(SECOND_STATION_NAME);
        Station station3 = createStation(THIRD_STATION_NAME);

        Sections sections = createSections(createSection(station1, station2)
                                            , createSection(station2, station3));

        // when, then
        assertThatThrownBy(() -> sections.deleteStation(station1))
                .isInstanceOf(SectionException.class)
                .hasMessage("마지막 하행역이 아닙니다.");
    }

    @DisplayName("구간이 하나면 제거 요청 시 예외")
    @Test
    void sectionSizeOneDeleteException() {
        // given
        Station station1 = createStation(FIRST_STATION_NAME);
        Station station2 = createStation(SECOND_STATION_NAME);

        Sections sections = createSections(createSection(station1, station2));

        // when, then
        assertThatThrownBy(() -> sections.deleteStation(station2))
                .isInstanceOf(SectionException.class)
                .hasMessage("구간이 하나라 삭제가 불가능합니다.");
    }
}
