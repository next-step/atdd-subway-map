package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class LineTest {

    @DisplayName("호선을 생성한다.")
    @Test
    void createLine() {

        final Line 신분당선 = new Line(1L, "신분당선", "bg-red-600");

        assertThat(신분당선).isEqualTo(new Line(1L, "신분당선", "bg-red-600"));
    }

    @DisplayName("호선 정보를 수정한다.")
    @Test
    void updateLine() {

        final Line 신분당선 = new Line(1L, "신분당선", "bg-red-600");
        신분당선.updateLine("2호선", "bg-green-600");

        assertAll(
                () -> assertThat(신분당선.getId()).isEqualTo(1L),
                () -> assertThat(신분당선.getName()).isEqualTo("2호선"),
                () -> assertThat(신분당선.getColor()).isEqualTo("bg-green-600")
        );
    }
}
