package subway.stationline;

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

}
