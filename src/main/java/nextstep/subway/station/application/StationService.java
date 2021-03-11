package nextstep.subway.station.application;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.domain.Stations;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class StationService {
    private StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        validateReduplicationStation(stationRequest);
        Station persistStation = stationRepository.save(stationRequest.toStation());
        return StationResponse.of(persistStation);
    }

    @Transactional(readOnly = true)
    public void validateReduplicationStation(StationRequest request) {
        Stations stations = Stations.of(stationRepository.findByName(request.getName()));
        stations.validateExistStation();
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStations() {
        Stations stations = Stations.of(stationRepository.findAll());
        return stations.toResponses();
    }

    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }
}
