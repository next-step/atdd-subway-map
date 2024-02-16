package subway.unit;

import org.junit.jupiter.api.Test;
import subway.domain.Line;
import subway.domain.Station;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {
    @Test
    void addSection() {
        // Line 인스턴스를 만들고
        // addSection 을 호출했을 때
        // Section 에 새로운 구간이 추가된다.

        Line line = new Line("이호선", "green");

        Station upStation = new Station(1L, "강남역");
        Station downStation = new Station(2L, "역삼역");
        Station newStation = new Station(3L, "선릉역");
        int distance = 10;

        line.addSection(upStation, downStation, distance);
        line.addSection(downStation, newStation, distance);

        assertThat(line.getSections()).hasSize(2);
    }

    @Test
    void deleteSection() {
        // Line 인스턴스를 만들고
        Line line = new Line("이호선", "green");

        Station upStation = new Station(1L,"강남역");
        Station downStation = new Station(2L, "역삼역");
        Station newStation = new Station(3L, "선릉역");
        int distance = 10;
        line.addSection(upStation, downStation, distance);
        line.addSection(downStation, newStation, distance);

        // deleteSection 을 호출했을 때
        line.deleteSection(newStation.getId());

        // Section 에 구간이 삭제된다.
        assertThat(line.getSections()).hasSize(1);
    }
}