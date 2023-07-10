package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.dto.station.StationRequest;
import subway.controller.dto.station.StationResponse;
import subway.model.station.Station;
import subway.model.station.StationService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StationCompositeService {
    private final StationService stationService;

    public StationCompositeService(StationService stationService) {
        this.stationService = stationService;
    }

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = stationService.save(Station.builder()
                                                     .name(stationRequest.getName())
                                                     .build());
        return createStationResponse(station);
    }

    public List<StationResponse> findAllStations() {
        return stationService.findAll()
                             .stream()
                             .map(this::createStationResponse)
                             .collect(Collectors.toList());
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationService.deleteById(id);
    }

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }
}
