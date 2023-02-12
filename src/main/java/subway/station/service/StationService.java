package subway.station.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.service.dto.StationRequest;
import subway.station.service.dto.StationResponse;
import subway.station.domain.station.Station;
import subway.station.domain.station.StationRepository;

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
    public StationResponse save(StationRequest stationRequest) {
        Station station = saveStation(stationRequest);
        return createStationResponse(station);
    }

    public List<StationResponse> findAllStations() {
        List<Station> stations = stationRepository.findAll();
        return toDtoFindAllResponse(stations);
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }

    private List<StationResponse> toDtoFindAllResponse(List<Station> stations) {
        return stations.stream()
                .map(this::createStationResponse)
                .collect(Collectors.toList());
    }

    private Station saveStation(StationRequest stationRequest) {
        return stationRepository.save(Station.builder()
                .name(stationRequest.getName())
                .build()
        );
    }

    private StationResponse createStationResponse(Station station) {
        return StationResponse.builder()
                .id(station.getId())
                .name(station.getName())
                .build();
    }
}
