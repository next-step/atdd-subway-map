package nextstep.subway.domain;

import nextstep.subway.exception.SectionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class SectionsTest extends SectionFixData {
    private static int DEFAULT_DISTANCE = 5;

    @DisplayName("구간 등록 시에 상행역이 아닌 하행역 등록은 예외")
    @Test
    void matchDownStationException() {
        // given
        Station station1 = createStation("강남역");
        Station station2 = createStation("역삼역");
        Station station3 = createStation("잠실역");
        Station station4 = createStation("잠실새내역");

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
        Station station1 = createStation("강남역");
        Station station2 = createStation("역삼역");
        Station station3 = createStation("잠실역");

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
        Station station1 = createStation("강남역");
        Station station2 = createStation("역삼역");
        Station station3 = createStation("잠실역");

        Sections sections = createSections(createSection(station1, station2), createSection(station2, station3));

        // when
        sections.deleteStation(station3);

        // then
        assertThat(sections.getStations().size()).isEqualTo(2);
    }


    @DisplayName("마지막 구간 하행역이 아니면 제거 요청 시 예외")
    @Test
    void notLastSectionDeleteException() {
        // given
        Station station1 = createStation("강남역");
        Station station2 = createStation("역삼역");
        Station station3 = createStation("잠실역");

        Sections sections = createSections(createSection(station1, station2), createSection(station2, station3));

        // when, then
        assertThatThrownBy(() -> sections.deleteStation(station3))
                .isInstanceOf(SectionException.class)
                .hasMessage("이미 구간에 등록되어 있습니다.");
    }


}
