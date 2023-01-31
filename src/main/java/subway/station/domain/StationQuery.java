package subway.station.domain;

import org.springframework.stereotype.Repository;
import subway.station.exception.StationNotFoundException;

@Repository
public class StationQuery {
    private final StationRepository stationRepository;

    public StationQuery(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Station findById(final Long id) {
        return stationRepository.findById(id)
                .orElseThrow(StationNotFoundException::new);
    }
}
