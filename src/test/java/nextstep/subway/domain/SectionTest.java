package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class SectionTest {
    private static int DEFAULT_DISTANCE = 5;

    @DisplayName("구간 생성")
    @Test
    void createSection() {
        // given
        Line line = createLine();
        Station upStation = createStation("강남역");
        Station downStation = createStation("역삼역");

        // when
        Section section = new Section(line, upStation, downStation, DEFAULT_DISTANCE);

        // then
        assertThat(section).isNotNull();
        assertThat(section.getDownStation()).isEqualTo(downStation);
        assertThat(section.getUpStation()).isEqualTo(upStation);
    }

    private Line createLine() {
        return new Line("2호선", "bg-green-700");
    }

    private Station createStation(String name) {
        return new Station(name);
    }
}
