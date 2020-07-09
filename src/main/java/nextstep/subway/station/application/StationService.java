package nextstep.subway.station.application;

import nextstep.subway.station.domain.Station;
import nextstep.subway.station.domain.StationRepository;
import nextstep.subway.station.dto.StationResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StationService {
    private final StationRepository stationRepository;

    public StationService(StationRepository stationRepository) {
        this.stationRepository = stationRepository;
    }

    @Transactional(readOnly = true)
    public List<StationResponse> findAllById(List<Long> ids) {
        return stationRepository.findAllById(ids).stream()
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<Station> findById(Long id) {
        return stationRepository.findById(id);
    }
}
