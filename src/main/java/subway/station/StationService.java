package subway.station;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import subway.SubwayService;
import subway.station.domain.Station;
import subway.station.request.StationRequest;
import subway.station.response.StationResponse;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StationService implements SubwayService<StationRequest, StationResponse> {
    private final StationRepository stationRepository;

    @Transactional
    @Override
    public StationResponse create(StationRequest stationRequest) {
        return this.saveStation(stationRequest);
    }

    private StationResponse saveStation(StationRequest stationRequest) {
        Station station = stationRepository.save(new Station(stationRequest.getName()));
        return this.createStationResponse(station);
    }

    public List<StationResponse> findAllStations() {
        return stationRepository.findAll().stream()
            .map(this::createStationResponse)
            .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }

    private StationResponse createStationResponse(Station station) {
        return StationResponse.builder()
            .id(station.getId())
            .name(station.getName())
            .build();
    }
}