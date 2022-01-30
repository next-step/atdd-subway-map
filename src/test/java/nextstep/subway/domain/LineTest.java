package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {
    @DisplayName("새로운 구간의 상행역은 현재 등록되어있는 하행 종점역이어야 한다.")
    @Test
    void validateUpStation() {
        // given
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 선릉역 = new Station("선릉역");
        Section section = new Section(강남역, 역삼역, 10);
        Line line = new Line("2호선", "bg-green-600");

        line.addSection(section);

        // when
        Section newSection = new Section(역삼역, 선릉역, 5);

        // then
        assertThat(line.validateUpStation(newSection.getUpStation())).isEqualTo(true);
    }

    @DisplayName("하행역은 현재 등록되어있는 역일 수 없다.")
    @Test
    void validateDownStation() {
        // given
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 선릉역 = new Station("선릉역");
        Section section = new Section(강남역, 역삼역, 10);
        Line line = new Line("2호선", "bg-green-600");

        line.addSection(section);

        // when
        Section newSection = new Section(역삼역, 강남역, 5);

        // then
        assertThat(line.validateDownStation(newSection.getDownStation())).isEqualTo(false);
    }

    @DisplayName("노선에 포함 된 지하철역 조회")
    @Test
    void getAllStations() {
        // given
        // given
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Station 선릉역 = new Station("선릉역");
        Section section1 = new Section(강남역, 역삼역, 10);
        Section section2 = new Section(역삼역, 선릉역, 5);
        Line line = new Line("2호선", "bg-green-600");

        line.addSection(section1);
        line.addSection(section2);

        // when
        List<StationResponse> allStations = line.getAllStations();

        assertThat(allStations).hasSize(3);
        assertThat(allStations.get(0).getName()).isEqualTo(강남역.getName());
        assertThat(allStations.get(1).getName()).isEqualTo(역삼역.getName());
        assertThat(allStations.get(2).getName()).isEqualTo(선릉역.getName());
    }

    @DisplayName("노선 구간 추가 ")
    @Test
    void 노선에_구간_추가() {
        Station 강남역 = new Station("강남역");
        Station 역삼역 = new Station("역삼역");
        Section section = new Section(강남역, 역삼역, 10);
        Line line = new Line("2호선", "bg-green-600");

        line.addSection(section);

        assertThat(line.getSections()).hasSize(1);
        assertThat(line.getAllStations()).hasSize(2);
    }
}