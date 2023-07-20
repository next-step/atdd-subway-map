package subway.station.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.dto.StationRequest;
import subway.station.repository.Station;
import subway.station.repository.StationRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public
class StationService {
    private final StationRepository stationRepository;

    StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public Station saveStation(StationRequest stationRequest) {
        return stationRepository.save(new Station(stationRequest.getName()));
    }

    public Station findStationById(Long stationId) {
        return stationRepository.findById(stationId).orElseThrow(() -> new RuntimeException("Not Exist Station"));
    }

    public List<Station> findAllStations() {
        return stationRepository.findAll();
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }
}
