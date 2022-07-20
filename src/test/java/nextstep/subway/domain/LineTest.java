package nextstep.subway.domain;

import nextstep.subway.acceptance.fixture.LineFixtures;
import nextstep.subway.acceptance.fixture.StationFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.acceptance.fixture.ColorFixtures.GREEN;
import static nextstep.subway.acceptance.fixture.ColorFixtures.RED;
import static nextstep.subway.acceptance.fixture.ColorFixtures.YELLOW;
import static nextstep.subway.acceptance.fixture.DistanceFixtures.TEN;
import static nextstep.subway.acceptance.fixture.LineFixtures.분당선;
import static nextstep.subway.acceptance.fixture.LineFixtures.신분당선;
import static org.assertj.core.api.Assertions.assertThat;

class LineTest {

    @Test
    @DisplayName("line 이름 및 컬러 수정")
    void updateNameAndColor() {
        // given
        Line line = new Line(분당선.getValue(), YELLOW.getValue());

        // when
        Line newLine = line.updateNameAndColor(신분당선.getValue(), RED.getValue());

        // then
        assertThat(newLine.getName()).isEqualTo(신분당선.getValue());
        assertThat(newLine.getColor()).isEqualTo(RED.getValue());
    }

    @Test
    @DisplayName("section 추가")
    void addSection() {
        // given
        int expectedSize = 2;
        Line 이호선 = new Line(LineFixtures.이호선.getValue(), GREEN.getValue());
        Station 강남역 = new Station(StationFixtures.강남역.getValue());
        Station 역삼역 = new Station(StationFixtures.역삼역.getValue());

        // when
        이호선.addSection(강남역, 역삼역, TEN.getValue());

        // then
        assertThat(convertToStationNames(이호선)).hasSize(expectedSize)
                .containsExactly(StationFixtures.강남역.getValue(), StationFixtures.역삼역.getValue());
    }

    private List<String> convertToStationNames(Line line) {
        return line.getStations()
                .stream()
                .map(Station::getName)
                .collect(Collectors.toList());
    }

}