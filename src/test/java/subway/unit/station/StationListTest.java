package subway.unit.station;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import subway.line.domain.Line;
import subway.line.domain.LineStationConnection;
import subway.station.domain.Station;
import subway.station.domain.StationList;

class StationListTest {

    @Test
    void updateLine() {
        //given
        Station station1 = new Station("판교역");
        Station station2 = new Station("정자역");
        StationList stationList = new StationList(Arrays.asList(station1, station2));

        //when
        Line line = new Line("신분당선", "abcd");
        List<LineStationConnection> connections = stationList.connectLineWithStation(line);

        //then
        assertThat(connections).hasSize(2);
        assertThat(connections.get(0).getStation()).isEqualTo(station1);
        assertThat(connections.get(0).getLine()).isEqualTo(line);

        assertThat(connections.get(1).getStation()).isEqualTo(station2);
        assertThat(connections.get(1).getLine()).isEqualTo(line);
    }

}
