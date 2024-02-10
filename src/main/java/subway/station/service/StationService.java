package subway.station.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.domain.Station;
import subway.station.domain.StationRepository;
import subway.station.presentation.request.CreateStationRequest;
import subway.station.presentation.response.CreateStationResponse;
import subway.station.presentation.response.ShowAllStationsResponse;

import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StationService {
    private StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public CreateStationResponse saveStation(CreateStationRequest createStationRequest) {
        Station station = stationRepository.save(new Station(createStationRequest.getName()));
        return CreateStationResponse.from(station);
    }

    public ShowAllStationsResponse findAllStations() {
        return ShowAllStationsResponse.of(stationRepository.findAll().stream()
                .map(StationDto::from)
                .collect(Collectors.toList()));
    }

    @Transactional
    public void deleteStationById(Long stationId) {
        stationRepository.deleteById(stationId);
    }

}
