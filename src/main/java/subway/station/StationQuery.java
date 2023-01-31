package subway.station;

import org.springframework.stereotype.Component;
import subway.exception.StationNotFoundException;

import java.util.List;

@Component
public class StationQuery {
    private StationRepository stationRepository;

    public StationQuery(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public List<Station> findAll() {
        return stationRepository.findAll();
    }

    public Station findById(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new StationNotFoundException(id));
    }
}
