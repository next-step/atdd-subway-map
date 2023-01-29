package subway.station;

import org.springframework.stereotype.Component;

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
        var findStation = stationRepository.findById(id);
        if (findStation.isPresent()) return findStation.get();
        throw new StationNotFoundException(id);
    }
}
