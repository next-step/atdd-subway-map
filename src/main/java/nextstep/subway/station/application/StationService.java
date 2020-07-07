package nextstep.subway.station.application;

import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StationService {
    private StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllById(List<Long> ids) {
        return stationRepository.findAllById(ids).stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }
}
