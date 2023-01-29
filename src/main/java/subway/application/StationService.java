package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Station;
import subway.domain.StationRepository;
import subway.application.dto.CreateStationRequest;
import subway.application.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StationService {
    private StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public StationResponse saveStation(CreateStationRequest createStationRequest) {
        Station station = stationRepository.save(new Station(createStationRequest.getName()));
        return new StationResponse(station);
    }

    public List<StationResponse> findAllStations() {
        return stationRepository.findAll().stream()
                .map(StationResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }

    Station findStationById(final long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(IllegalArgumentException::new);
    }
}
