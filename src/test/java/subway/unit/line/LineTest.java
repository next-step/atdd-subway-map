package subway.unit.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;
import subway.line.domain.Line;
import subway.line.domain.LineLastStations;
import subway.section.domain.Section;
import subway.section.domain.SectionStations;
import subway.station.domain.Station;

class LineTest {

    private final Station stationA = new Station("a");
    private final Station stationB = new Station("b");
    private final Station stationC = new Station("c");
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

    @Test
    void addBaseSection() {
        Line line = new Line("신분당선", "abc", lastStations);

        line.addBaseSection(5);

        List<Section> sectionList = line.getSections();
        assertThat(sectionList).hasSize(1);
    }

    @Test
    void addSection() {
        Line line = new Line("신분당선", "abc", lastStations);

        Section section = new Section(line, new SectionStations(stationB, stationC), 3);
        line.addSection(section);

        List<Section> sections = line.getSections();
        LineLastStations stations = line.getLastStations();
        assertThat(sections).hasSize(1);
        assertThat(stations.getDownLastStation().getName()).isEqualTo(stationC.getName());
    }

    @Test
    void addSectionThrowException() {
        Line line = new Line("신분당선", "abc", lastStations);

        Section sectionA = new Section(line, new SectionStations(stationC, stationB), 3);
        Section sectionB = new Section(line, new SectionStations(stationB, stationA), 3);

        assertThrows(IllegalArgumentException.class, () -> line.addSection(sectionA));
        assertThrows(IllegalArgumentException.class, () -> line.addSection(sectionB));
    }
}
