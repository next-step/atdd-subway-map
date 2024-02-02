package subway.unit;

import org.junit.jupiter.api.Test;
import subway.domain.entity.Station;
import subway.domain.entity.Line;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {
    @Test
    void addSection() {
        // line 인스턴스를 만들고
        // addSection 을 호출했을 때

        Line line = new Line("2호선", "green");

        Station upStation = new Station("강남역");
        Station downStation = new Station("역삼역");
        Station newStation = new Station("역삼역");

        int distance = 10;

        line.addSection(upStation, downStation, distance);
        line.addSection(downStation, newStation, distance);

        assertThat(line.getSections()).hasSize(2);
    }
}
