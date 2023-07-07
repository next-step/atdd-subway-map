package subway.unit.station;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import subway.line.domain.Line;
import subway.line.domain.LineStationConnection;
import subway.station.domain.Station;

class LineConnectionTest {

    @Test
    void createConnectionsList() {
        Station station =  new Station("abc");
        Station station1 =  new Station("abc1");

        Line line = new Line("ddd", "color");

        List<LineStationConnection> connectionList = LineStationConnection.createConnectionsList(
                Arrays.asList(station, station1), line);

        assertThat(connectionList).hasSize(2);
        LineStationConnection connection = connectionList.get(0);
        LineStationConnection connection1 = connectionList.get(1);
        assertThat(connection.getStation().getName()).isEqualTo("abc");
        assertThat(connection1.getStation().getName()).isEqualTo("abc1");
    }

}
