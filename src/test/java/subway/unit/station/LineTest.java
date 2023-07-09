package subway.unit.station;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import subway.line.domain.Line;

class LineTest {

    @Test
    void updateName() {
        Line line = new Line("신분당선", "abc");

        line.updateName("신신분당선");

        assertThat(line.getName()).isEqualTo("신신분당선");
    }

    @Test
    void updateNameException() {
        Line line = new Line("신분당선", "abc");
        assertThrows(IllegalArgumentException.class, () -> line.updateName(""));
        assertThrows(IllegalArgumentException.class, () -> line.updateName(null));
    }

    @Test
    void updateColor() {
        Line line = new Line("신분당선", "abc");

        line.updateColor("abcd");

        assertThat(line.getColor()).isEqualTo("abcd");
    }

    @Test
    void updateColorException() {
        Line line = new Line("신분당선", "abc");
        assertThrows(IllegalArgumentException.class, () -> line.updateColor(""));
        assertThrows(IllegalArgumentException.class, () -> line.updateColor(null));
    }

}
