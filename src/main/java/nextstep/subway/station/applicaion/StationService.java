package nextstep.subway.station.applicaion;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.station.domain.dto.StationRequest;
import nextstep.subway.station.domain.dto.StationResponse;
import nextstep.subway.station.domain.model.Station;
import nextstep.subway.station.domain.repository.StationRepository;
import nextstep.subway.common.exception.ColumnName;
import nextstep.subway.common.exception.DuplicateColumnException;
import nextstep.subway.common.exception.OptionalException;

@Service
@Transactional
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationResponse saveStation(StationRequest request) {
        verifyExistsByName(request.getName()).verify();
        Station station = stationRepository.save(new Station(request.getName()));
        return StationResponse.from(station);
    }

    private OptionalException<DuplicateColumnException> verifyExistsByName(String name) {
        if (stationRepository.existsByName(name)) {
            return OptionalException.of(
                new DuplicateColumnException(ColumnName.STATION_NAME)
            );
        }
        return OptionalException.empty();
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStations() {
        List<Station> stations = stationRepository.findAll();

        return stations.stream()
                       .map(StationResponse::from)
                       .collect(Collectors.toList());
    }

    public void deleteStationById(Long id) {
        stationRepository.deleteById(id);
    }
}
