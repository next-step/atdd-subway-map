package subway.unit.station;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import subway.line.domain.LineLastStations;
import subway.station.domain.Station;

class LineLastStationsTest {

    private final Station stationA = new Station("a");
    private final Station stationB = new Station("b");

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
}
