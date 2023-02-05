package subway.station;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StationQuery {
    private StationRepository stationRepository;

    public StationQuery(StationRepository stationRepository) { this.stationRepository = stationRepository; }

    public List<Station> findByIds(List<Long> ids) {
        return stationRepository.findAllById(ids);
    }
}
