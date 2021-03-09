package nextstep.subway.station.application;

import nextstep.subway.common.exception.ApplicationException;
import nextstep.subway.common.exception.ApplicationType;
import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationRequest;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class StationService {
    private StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        Station persistStation = stationRepository.save(stationRequest.toStation());
        return StationResponse.of(persistStation);
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStations() {
        List<Station> stations = stationRepository.findAll();

        return stations.stream()
                .map(station -> StationResponse.of(station))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StationResponse findStationById(long id) {
        Optional<Station> line = stationRepository.findOneById(id);

        return line.map(StationResponse::of).orElse(null);
    }

    public void validateStationId(Long id) {
        Optional<Station> station = stationRepository.findOneById(id);
        station.orElseThrow(() -> new ApplicationException(ApplicationType.INVALID_ID));
    }

    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }
}
