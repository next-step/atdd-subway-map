package subway.station.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.exception.LineNotFoundException;
import subway.station.repository.StationCommandRepository;
import subway.station.dto.StationRequest;
import subway.station.dto.StationResponse;
import subway.station.domain.Station;
import subway.station.repository.StationQueryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StationService {

    private final StationCommandRepository stationCommand;
    private final StationQueryRepository stationQuery;

    @Transactional
    public Station saveStation(StationRequest stationRequest) {
        Station station = stationCommand.save(new Station(stationRequest.getName()));
        return station;
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationCommand.deleteById(id);
    }

    public Station findStationById(Long id) {
        return stationQuery.findById(id).orElseThrow(LineNotFoundException::new);
    }

    public List<Station> findAllStations() {
        return stationQuery.findAll();
    }

}