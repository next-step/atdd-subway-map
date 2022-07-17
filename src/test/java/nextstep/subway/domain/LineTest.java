package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class LineTest {

    @Test
    @DisplayName("line 이름 및 컬러 수정")
    void updateNameAndColor() {
        // given
        String 신분당선 = "신분당선";
        String color = "red";
        Line line = new Line("분당선", "yellow");

        // when
        Line newLine = line.updateNameAndColor(신분당선, color);

        // then
        assertThat(newLine.getName()).isEqualTo(신분당선);
        assertThat(newLine.getColor()).isEqualTo(color);
    }

    @Test
    @DisplayName("section 추가")
    void addSection() {
        // given
        int expectedSize = 2;
        Line 이호선 = new Line("2호선", "green");
        String 강남역 = "강남역";
        String 역삼역 = "역삼역";
        Section 강남역섹션 = new Section(new Station(강남역), 이호선, new Distance(0, 10));
        Section 역삼역섹션 = new Section(new Station(역삼역), 이호선, new Distance(10, 0));

        // when
        이호선.addSections(강남역섹션, 역삼역섹션);

        // then
        assertThat(convertToStationNames(이호선)).hasSize(expectedSize)
                .containsExactly(강남역, 역삼역);
    }

    private List<String> convertToStationNames(Line line) {
        return line.getStations()
                .stream()
                .map(Station::getName)
                .collect(Collectors.toList());
    }

}