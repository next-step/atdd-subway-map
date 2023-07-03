package subway.unit.station;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import subway.line.domain.Line;
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
        stationList.updateLine(line);

        //then
        assertThat(station1.getLine().getName()).isEqualTo("신분당선");
        assertThat(station2.getLine().getName()).isEqualTo("신분당선");
    }

}
