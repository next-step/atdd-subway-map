package subway.unit.station;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import subway.line.domain.Line;
import subway.line.domain.LineLastStations;
import subway.station.domain.Station;

class LineTest {

    private final Station stationA = new Station("a");
    private final Station stationB = new Station("a");
    private final LineLastStations lastStations = new LineLastStations(stationA, stationB);

    @Test
    void updateName() {
        Line line = new Line("신분당선", "abc", lastStations);

        line.updateName("신신분당선");

        assertThat(line.getName()).isEqualTo("신신분당선");
    }

    @Test
    void updateNameException() {
        Line line = new Line("신분당선", "abc", lastStations);
        assertThrows(IllegalArgumentException.class, () -> line.updateName(""));
        assertThrows(IllegalArgumentException.class, () -> line.updateName(null));
    }

    @Test
    void updateColor() {
        Line line = new Line("신분당선", "abc", lastStations);

        line.updateColor("abcd");

        assertThat(line.getColor()).isEqualTo("abcd");
    }

    @Test
    void updateColorException() {
        Line line = new Line("신분당선", "abc", lastStations);
        assertThrows(IllegalArgumentException.class, () -> line.updateColor(""));
        assertThrows(IllegalArgumentException.class, () -> line.updateColor(null));
    }

}
