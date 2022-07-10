package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
}