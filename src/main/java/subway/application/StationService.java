package subway.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.domain.Station;
import subway.domain.StationRepository;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.exception.StationNotFoundException;

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
        return new StationResponse(station);
    }

    public List<StationResponse> findAllStations() {
        return stationRepository.findAll().stream()
                .map(StationResponse::new)
                .collect(Collectors.toList());
    }

    public Station findById(Long id) {
        return stationRepository.findById(id)
            .orElseThrow(() -> new StationNotFoundException(id));
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }
}
