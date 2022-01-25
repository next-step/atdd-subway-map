package nextstep.subway.domain;

import nextstep.subway.applicaion.Section;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {
    @DisplayName("새로운 구간의 상행역은 현재 등록되어있는 하행 종점역이어야 한다.")
    @Test
    void validateUpStation() {
        // given
        Station station1 = new Station("강남역");
        Station station2 = new Station("역삼역");
        Station station3 = new Station("선릉역");
        Section section = new Section(station1, station2, 10);
        Line line = new Line("2호선", "bg-green-600");

        line.addSection(section);

        // when
        Section newSection = new Section(station2, station3, 5);

        // then
        assertThat(line.validateUpStation(newSection.getUpStation())).isEqualTo(true);
    }

    @DisplayName("하행역은 현재 등록되어있는 역일 수 없다.")
    @Test
    void validateDownStation() {
        // given
        Station station1 = new Station("강남역");
        Station station2 = new Station("역삼역");
        Station station3 = new Station("선릉역");
        Section section = new Section(station1, station2, 10);
        Line line = new Line("2호선", "bg-green-600");

        line.addSection(section);

        // when
        Section newSection = new Section(station2, station1, 5);

        // then
        assertThat(line.validateDownStation(newSection.getDownStation())).isEqualTo(false);
    }
}