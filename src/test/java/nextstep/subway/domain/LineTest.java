package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LineTest {

    @DisplayName("지하철 노선에 구간을 추가할 수 있다")
    @Test
    void addSectionSuccess() {
        final Line 분당선 = new Line("분당선", "bg-green-600", 1L, 2L, 10);
        분당선.addSection(new Section(분당선, 2L, 3L, 5));

        assertThat(분당선.getUpStationId()).isEqualTo(1L);
        assertThat(분당선.getDownStationId()).isEqualTo(3L);
        assertThat(분당선.getDistance()).isEqualTo(15);
    }

    @DisplayName("새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.")
    @Test
    void addSectionFail1() {
        final Line 분당선 = new Line("분당선", "bg-green-600", 1L, 2L, 10);

        assertThatThrownBy(() -> 분당선.addSection(new Section(분당선, 3L, 4L, 5)))
                .hasMessage("새로운 구간의 상행역은 해당 노선에 등록되어 있는 하행 종점역이어야 한다.");
    }

    @DisplayName("새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.")
    @Test
    void addSectionFail2() {
        final Line 분당선 = new Line("분당선", "bg-green-600", 1L, 2L, 10);
        분당선.addSection(new Section(분당선, 2L, 3L, 5));

        assertThatThrownBy(() -> 분당선.addSection(new Section(분당선, 3L, 2L, 5)))
                .hasMessage("새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.");
    }

}