package nextstep.subway.applicaion;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.exception.NotFoundStationException;
import nextstep.subway.exception.StationNameDuplicationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StationService {

    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationResponse saveStation(StationRequest stationRequest) {
        if (stationRepository.existsByName(stationRequest.getName())) {
            throw new StationNameDuplicationException(stationRequest.getName());
        }

        Station station = stationRepository.save(new Station(stationRequest.getName()));

        return createStationResponse(station);
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStations() {
        List<Station> stations = stationRepository.findAll();

        return stations.stream()
            .map(this::createStationResponse)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Station findStation(Long id) {
        return stationRepository.findById(id)
            .orElseThrow(() -> new NotFoundStationException(id));
    }

    public void deleteStation(Long id) {
        stationRepository.deleteById(id);
    }

    private StationResponse createStationResponse(Station station) {
        return StationResponse.from(station);
    }

}
