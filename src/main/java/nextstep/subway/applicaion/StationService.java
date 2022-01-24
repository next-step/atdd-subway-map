package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.exception.station.StationDuplicateNameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StationService {
    private final StationRepository stationRepository;

    public StationService(final StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationResponse saveStation(final StationRequest stationRequest) {
        validateDuplicatedStationName(stationRequest.getName());
        final Station station = stationRepository.save(new Station(stationRequest.getName()));
        return new StationResponse(station);
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStations() {
        final List<Station> stations = stationRepository.findAll();

        return stations.stream()
                .map(StationResponse::new)
                .collect(Collectors.toList());
    }

    public void deleteStationById(final Long id) {
        stationRepository.deleteById(id);
    }

    private void validateDuplicatedStationName(final String name) {
        if (stationRepository.existsByName(name)) {
            throw new StationDuplicateNameException();
        }
    }
}
