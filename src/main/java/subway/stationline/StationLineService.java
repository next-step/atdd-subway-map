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

    public StationLineResponse findById(long id) {
        return StationLineResponse.of(findEntityById(id));
    }

    private StationLine findEntityById(long id) {
        return repository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("해당하는 id 에 맞는 지하철 노선이 존재하지 않습니다.")
        );
    }

    public void modify(long id, StationLineModifyRequest request) {

        StationLine stationLine = findEntityById(id);
        stationLine.updateName(request.getName());
        stationLine.updateColor(request.getColor());

        repository.save(stationLine);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }
}
