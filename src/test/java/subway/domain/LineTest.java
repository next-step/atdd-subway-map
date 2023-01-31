package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("노선 도메인")
class LineTest {

    @DisplayName("노선의 이름과 색을 수정한다.")
    @Test
    void updateTest() {
        Line line = new Line("A", "red");

        line.update("B", "blue");

        assertAll(() -> {
            assertThat(line.getName()).isEqualTo("B");
            assertThat(line.getColor()).isEqualTo("blue");
        });
    }
}
