package subway.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.domain.Station;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.exception.StationNotFoundException;
import subway.repository.StationRepository;

@Service
@Transactional(readOnly = true)
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = stationRepository.save(new Station(stationRequest.getName()));
        return StationResponse.createStationResponse(station);
    }

    public Station findStation(Long stationId) {
        return stationRepository.findById(stationId)
            .orElseThrow(StationNotFoundException::new);
    }

    public List<StationResponse> findAllStations() {
        return stationRepository.findAll().stream()
            .map(StationResponse::createStationResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }
}
