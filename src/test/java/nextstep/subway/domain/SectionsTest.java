package nextstep.subway.domain;

import nextstep.subway.exception.SectionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@DisplayName("구간들 관리")
public class SectionsTest extends SectionFixData {

    @DisplayName("새로운 구간 생성할 때 노선의 마지막 하행역이 아닌 상행역이면 예외처리")
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
        assertThatThrownBy(() -> sections.addSection(section))
                .isInstanceOf(SectionException.class)
                .hasMessage("하행역만 상행역으로 등록될 수 있습니다.");
    }

    @DisplayName("노선에 존재하는 역은 중복이면 예외처리")
    @Test
    void matchAllStationException() {
        // given
        Station station1 = createStation(FIRST_STATION_NAME);
        Station station2 = createStation(SECOND_STATION_NAME);
        Station station3 = createStation(THIRD_STATION_NAME);

        Sections sections = createSections(createSection(station1, station2), createSection(station2, station3));
        Section section = createSection(station3, station1);

        // when, then
        assertThatThrownBy(() -> sections.addSection(section))
                .isInstanceOf(SectionException.class)
                .hasMessage("이미 구간에 등록되어 있습니다.");
    }

    @DisplayName("구간에서 마지막 하행역만 삭제한다.")
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
        assertThat(sections.getStations()).hasSize(2);
    }

    @DisplayName("마지막 구간의 하행역이 아니면 삭제처리 시 예외처리.")
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

    @DisplayName("구간이 하나면 삭제 요청 시 예외처리")
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
