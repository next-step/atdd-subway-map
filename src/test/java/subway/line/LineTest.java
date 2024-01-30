package subway.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.station.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {

    @Test
    @DisplayName("생성된 라인에 구간을 더할 수 있다")
    void addSection1() {
        Line line = new Line("신분당선",
                "bg-red-600",
                new Station(1L, "강남역"),
                new Station(2L, "선릉역"),
                10L);
        Section input = new Section(
                new Station(2L, "선릉역"),
                new Station("교대역"),
                5L);
        line.addSection(input);

        Sections actual = line.getSections();
        Sections expected = Sections.from(
                List.of(new Section(new Station(1L, "강남역"),
                                new Station(2L, "선릉역"),
                                10L),
                        new Section(new Station(2L, "선릉역"),
                                new Station("교대역"),
                                5L)));
        assertThat(actual).isEqualTo(expected);

        Long actualDistance = line.getDistance();
        Long expectedDistance = 15L;
        assertThat(actualDistance).isEqualTo(expectedDistance);
    }

}
