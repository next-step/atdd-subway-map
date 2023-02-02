package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.domain.Station;
import subway.domain.StationRepository;

@Service
@Transactional(readOnly = true)
public class StationService {

    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public StationResponse saveStation(final StationRequest stationRequest) {
        Station station = stationRepository.save(new Station(stationRequest.getName()));
        return StationResponse.by(station);
    }

    public List<StationResponse> findAllStations() {
        return stationRepository.findAll().stream()
                .map(StationResponse::by)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(final Long id) {
        stationRepository.deleteById(id);
    }
}
