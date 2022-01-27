package nextstep.subway.station.applicaion;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import nextstep.subway.common.exception.ColumnName;
import nextstep.subway.common.exception.DuplicateColumnException;
import nextstep.subway.station.domain.dto.StationRequest;
import nextstep.subway.station.domain.dto.StationResponse;
import nextstep.subway.station.domain.model.Station;
import nextstep.subway.station.domain.repository.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public StationResponse saveStation(StationRequest request) {
        if (stationRepository.existsByName(request.getName())) {
            throw new DuplicateColumnException(ColumnName.STATION_NAME);
        }
        Station station = stationRepository.save(new Station(request.getName()));
        return StationResponse.from(station);
    }

    @Transactional(readOnly = true)
    public Station findById(long id) {
        return stationRepository.findById(id)
                                .orElseThrow(EntityNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllStations() {
        List<Station> stations = stationRepository.findAll();

        return stations.stream()
                       .map(StationResponse::from)
                       .collect(Collectors.toList());
    }

    public void deleteStationById(long id) {
        stationRepository.deleteById(id);
    }
}
