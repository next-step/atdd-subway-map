package subway;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.jdbc.Sql;

import subway.station.Station;
import subway.station.StationRepository;

@Sql(scripts = {
        "classpath:sql/schema.sql",
        "classpath:sql/station_init.sql"
})
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class StationServiceTest {

    @Autowired
    private StationRepository stationRepository;

    @Test
    void testSql() {
        Station giveStation = stationRepository.findById(1L).get();

        Assertions.assertThat(giveStation.getName()).isEqualTo("강남역");
    }
}
