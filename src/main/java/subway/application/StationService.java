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

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public StationResponse saveStation(final StationRequest stationRequest) {
        Station station = stationRepository.save(new Station(stationRequest.getName()));
        return StationResponse.from(station);
    }

    public List<StationResponse> findAllStations() {
        return stationRepository.findAll().stream()
                .map(StationResponse::from)
                .collect(Collectors.toList());
    }

    public List<Station> findAllById(final List<Long> ids) {
        return stationRepository.findAllById(ids);
    }

    @Transactional
    public void deleteStationById(final Long id) {
        Station station = stationRepository.findById(id)
                .orElseThrow(StationNotFoundException::new);

        stationRepository.delete(station);
    }
}
