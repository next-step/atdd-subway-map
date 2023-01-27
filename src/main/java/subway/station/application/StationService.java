package subway.station.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.domain.Station;
import subway.station.domain.StationRepository;
import subway.station.presentation.dto.request.StationRequest;
import subway.station.presentation.dto.response.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

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
        return createStationResponse(station);
    }

    public List<StationResponse> findAllStations() {
        return stationRepository.findAll().stream()
                .map(this::createStationResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(final Long id) {
        stationRepository.deleteById(id);
    }

    private StationResponse createStationResponse(final Station station) {
        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }
}
