package subway.domain;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class StationRepositoryTest {

    @Autowired
    StationRepository stationRepository;

    @AfterEach
    public void cleanup() {
        stationRepository.deleteAll();
    }

    @DisplayName("생성한 지하철 역을 불러온다.")
    @Test
    void findAllStations() {
        String name = "양재역";

        stationRepository.save(Station.builder()
                .name(name)
                .build());

        List<Station> stations = stationRepository.findAll();
        Station station = stations.get(0);

        assertThat(station.getName()).isEqualTo(name);
    }
}
