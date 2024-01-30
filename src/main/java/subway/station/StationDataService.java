package subway.station;

import org.springframework.stereotype.Service;
import subway.line.exception.LineException;

import javax.transaction.Transactional;

@Transactional
@Service
public class StationDataService {

    private final StationRepository stationRepository;

    public StationDataService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public Station findStation(Long id) {
        return stationRepository.findById(id)
                .orElseThrow(() -> new LineException("존재하지 않는 역입니다."));
    }

    public StationResponse mappingToStationResponse(Station station) {
        return new StationResponse(station.getId(), station.getName());
    }
}
