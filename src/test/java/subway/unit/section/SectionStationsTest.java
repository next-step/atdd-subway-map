package subway.unit.section;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import subway.section.domain.SectionStations;
import subway.station.domain.Station;

class SectionStationsTest {

    @Test
    void create() {
        Station a = new Station("a");
        assertThrows(IllegalArgumentException.class, ()-> new SectionStations(a, a));
    }

}
