package subway.initializer;

import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import subway.domain.Station;
import subway.repository.StationRepository;

@Component
public class StationInitializer implements CommandLineRunner {

    private final StationRepository stationRepository;

    public StationInitializer(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        List<Station> stations = List.of(
            new Station(1L, "강남역"),
            new Station(2L, "광교역"),
            new Station(3L, "양재역"),
            new Station(4L, "논현역"),
            new Station(5L, "신논현역")
            );

        stationRepository.saveAll(stations);
    }
}
