package subway.stationline;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StationLineService {

    private final StationLineRepository repository;

    public StationLineService(StationLineRepository repository) {
        this.repository = repository;
    }

    public StationLineResponse create(StationLineRequest request) {

        return StationLineResponse.of(repository.save(
            new StationLine(
                request.getName(),
                request.getColor(),
                request.getUpStationId(),
                request.getDownStationId(),
                request.getDistance()
            )
        ));
    }

    public List<StationLineResponse> findAllStationLines() {

        return repository.findAll().stream()
            .map(StationLineResponse::of)
            .collect(Collectors.toList());
    }
}
