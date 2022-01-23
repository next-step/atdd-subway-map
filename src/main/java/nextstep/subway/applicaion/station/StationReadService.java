package nextstep.subway.applicaion.station;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class StationReadService {
    private final StationRepository stationRepository;

    public StationReadService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    public List<StationResponse> findAllStations() {
        List<Station> stations = stationRepository.findAll();

        return stations.stream().map(StationResponse::of).collect(Collectors.toList());
    }
}
