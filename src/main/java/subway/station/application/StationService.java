package subway.station.application;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import subway.line.exception.LineException;
import subway.line.exception.LineExceptionType;
import subway.station.domain.Station;
import subway.station.application.dto.StationRequest;
import subway.station.application.dto.StationResponse;
import subway.station.domain.StationRepository;
import subway.station.exception.StationException;
import subway.station.exception.StationExceptionType;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class StationService {

    private final StationRepository stationRepository;

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        Station station = stationRepository.save(new Station(stationRequest.getName()));
        return createStationResponse(station);
    }

    public List<StationResponse> findAllStations() {
        return stationRepository.findAll().stream()
                .map(this::createStationResponse)
                .collect(Collectors.toList());
    }

    public StationResponse findStation(Long stationId) {
        Station station = findById(stationId);

        return createStationResponse(station);
    }

    private Station findById(Long stationId) {
        return stationRepository.findById(stationId)
            .orElseThrow(() -> new StationException(StationExceptionType.STATION_NOT_FOUND));
    }

    @Transactional
    public void deleteStationById(Long stationId) {
        Station station = findById(stationId);
        stationRepository.deleteById(station.getId());
    }

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(
                station.getId(),
                station.getName()
        );
    }
}
