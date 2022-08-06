package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.StationRequest;
import nextstep.subway.applicaion.dto.StationResponse;
import nextstep.subway.domain.Station;
import nextstep.subway.domain.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StationQueryService {
    private static final String STATION_NOTFOUND_MESSAGE = "해당 id의 지하철 역이 존재하지 않습니다.";

    private final StationRepository stationRepository;

    public List<StationResponse> findAllStations() {
        return stationRepository.findAll().stream()
                .map(this::createStationResponse)
                .collect(Collectors.toList());
    }

    private StationResponse createStationResponse(Station station) {
        return new StationResponse(station);
    }

    public Station findById(Long id) {
        if (id == null) {
            return new Station();
        }

        return stationRepository.findById(id).orElseThrow(() -> new IllegalArgumentException(STATION_NOTFOUND_MESSAGE));
    }
}
