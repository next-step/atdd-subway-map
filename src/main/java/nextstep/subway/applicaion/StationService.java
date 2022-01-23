package nextstep.subway.applicaion;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import nextstep.subway.exception.ColumnName;
import nextstep.subway.exception.DuplicateColumnException;
import nextstep.subway.utils.ExceptionOptional;

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

    private ExceptionOptional<DuplicateColumnException> verifyExistsByName(String name) {
        if (stationRepository.existsByName(name)) {
            return ExceptionOptional.of(
                new DuplicateColumnException(ColumnName.STATION_NAME)
            );
        }
        return ExceptionOptional.empty();
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
