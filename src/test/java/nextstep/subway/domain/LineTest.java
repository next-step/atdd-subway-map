package nextstep.subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 단위 테스트")
class LineTest {
    @DisplayName("지하철 노선 section 추가시 Line 매핑")
    @Test
    void createSection() {
        // given
        final Line line = new Line();
        final Station 상행역 = new Station("상행역");
        final Station 하행역 = new Station("하행역");
        final int distance = 1;

        // when
        line.addSection(상행역, 하행역, distance);

        // then
        assertThat(line.getSections()
                .sections()
                .get(0)
                .getLine()).isEqualTo(line);
    }
}