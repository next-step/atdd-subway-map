package nextstep.subway.domain;

import nextstep.subway.acceptance.fixture.LineFixtures;
import nextstep.subway.acceptance.fixture.StationFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.acceptance.fixture.ColorFixtures.GREEN;
import static nextstep.subway.acceptance.fixture.DistanceFixtures.TEN;
import static org.assertj.core.api.Assertions.assertThat;

class SectionsTest {

    @Test
    @DisplayName("section 추가")
    void addSection() {
        // given
        int expectedSize = 2;
        Sections sections = new Sections();
        Line 이호선 = new Line(LineFixtures.이호선.getValue(), GREEN.getValue());
        Station 강남역 = new Station(StationFixtures.강남역.getValue());
        Station 역삼역 = new Station(StationFixtures.역삼역.getValue());
        Section section = new Section(이호선, 강남역, 역삼역, TEN.getValue());

        // when
        sections.addSection(section);

        // then
        List<String> stationNames = getStationNames(sections);
        assertThat(stationNames).hasSize(expectedSize)
                .containsExactly(StationFixtures.강남역.getValue(), StationFixtures.역삼역.getValue());
    }

    private List<String> getStationNames(Sections sections) {
        return sections.getStations()
                .stream()
                .map(Station::getName)
                .collect(Collectors.toList());
    }

}