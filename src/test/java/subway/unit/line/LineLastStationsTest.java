package subway.unit.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import subway.line.domain.LineLastStations;
import subway.section.domain.SectionStations;
import subway.station.domain.Station;

class LineLastStationsTest {

    private final Station stationA = new Station("a");
    private final Station stationB = new Station("b");
    private final Station stationC = new Station("C");

    @Test
    void createLineLastStation() {
        List<Station> stationList = Arrays.asList(stationA, stationB);

        LineLastStations stations = LineLastStations.createLineLastStation(stationList);

        assertThat(stations.getUpLastStation().getName()).isEqualTo("a");
        assertThat(stations.getDownLastStation().getName()).isEqualTo("b");
    }

    @Test
    void createLineLastStationException() {
        List<Station> stationList = List.of(stationA);
        List<Station> sameStation = Arrays.asList(stationA, stationA);
        List<Station> emptyList = new ArrayList<>();
        assertThrows(IllegalArgumentException.class, () -> LineLastStations.createLineLastStation(stationList));
        assertThrows(IllegalArgumentException.class, () -> LineLastStations.createLineLastStation(sameStation));
        assertThrows(IllegalArgumentException.class, () -> LineLastStations.createLineLastStation(emptyList));
    }

    @Test
    void checkCanAddSection() {
        LineLastStations stations = new LineLastStations(stationA, stationB);
        SectionStations sectionStationsA = new SectionStations(stationB, stationC);
        SectionStations sectionStationsB = new SectionStations(stationA, stationC);
        SectionStations sectionStationsC = new SectionStations(stationB, stationA);

        assertThat(stations.checkCanAddSection(sectionStationsA)).isTrue();
        assertThat(stations.checkCanAddSection(sectionStationsB)).isFalse();
        assertThat(stations.checkCanAddSection(sectionStationsC)).isFalse();
    }

    @Test
    void updateDownStation() {
        LineLastStations stations = new LineLastStations(stationA, stationB);

        stations.updateDownLastStation(stationC);

        assertThat(stations.getDownLastStation()).isEqualTo(stationC);
    }
}
