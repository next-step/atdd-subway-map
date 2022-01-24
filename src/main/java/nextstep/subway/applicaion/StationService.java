package nextstep.subway.applicaion;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.exception.DuplicateStationException;
import nextstep.subway.exception.StationNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional
    public StationResponse saveStation(StationRequest stationRequest) {
        boolean existsStationByName = stationRepository.existsByName(stationRequest.getName());
        if (existsStationByName) {
            throw new DuplicateStationException(stationRequest.getName());
        }

        Station station = stationRepository.save(new Station(stationRequest.getName()));
        return createStationResponse(station);
    }

    public List<StationResponse> findAllStations() {
        List<Station> stations = stationRepository.findAll();

        return stations.stream()
            .map(this::createStationResponse)
            .collect(Collectors.toList());
    }

    public StationResponse showStationById(Long id) {
        Station station = findStationsById(id);
        return new StationResponse(station.getId(), station.getName(),
            station.getCreatedDate(), station.getModifiedDate());
    }

    public Station findStationsById(Long id) {
        return stationRepository.findById(id)
            .orElseThrow(() -> new StationNotFoundException(id));
    }

    @Transactional
    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(
            station.getId(),
            station.getName(),
            station.getCreatedDate(),
            station.getModifiedDate()
        );
    }

}
