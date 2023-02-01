package subway.station.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.station.repository.StationCommandRepository;
import subway.station.controller.dto.StationRequest;
import subway.station.controller.dto.StationResponse;
import subway.station.domain.Station;
import subway.station.repository.StationQueryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class StationService {

    private final StationCommandRepository stationCommand;
    private final StationQueryRepository stationQuery;

    public StationService(final StationCommandRepository stationCommand, final StationQueryRepository stationQuery) {
        this.stationCommand = stationCommand;
        this.stationQuery = stationQuery;
    }

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = stationCommand.save(new Station(stationRequest.getName()));
        return createStationResponse(station);
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationCommand.deleteById(id);
    }

    public List<StationResponse> findAllStations() {
        return stationQuery.findAll().stream()
                .map(this::createStationResponse)
                .collect(Collectors.toList());
    }

    private StationResponse createStationResponse(Station station) {
        return StationResponse.fromDomain(station);
    }

}