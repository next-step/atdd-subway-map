package subway.subway.adapter.out.persistence;

import org.springframework.stereotype.Component;
import subway.subway.adapter.out.persistence.repository.StationRepository;
import subway.subway.application.out.StationClosePort;
import subway.subway.domain.Station;

@Component
public class StationClosePersistenceAdapter implements StationClosePort {
    private final StationRepository stationRepository;

    public StationClosePersistenceAdapter(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Override
    public void closeStation(Station.Id id) {
        stationRepository.deleteById(id.getValue());
    }
}
