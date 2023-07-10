package subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.entity.StationLine;
import subway.repository.StationLineRepository;
import subway.service.request.StationLineRequest;

@Service
@Transactional
public class StationLineService {

    private final StationLineRepository repository;

    public StationLineService(StationLineRepository repository) {
        this.repository = repository;
    }

    public StationLine create(StationLineRequest request) {

        return repository.save(
            new StationLine(
                request.getName(),
                request.getColor()
            )
        );
    }

    public List<StationLine> findAllLine() {

        return repository.findAll();
    }

    public StationLine findById(long id) {
        return repository.findById(id).orElseThrow(
            () -> new IllegalArgumentException("해당하는 id 에 맞는 지하철 노선이 존재하지 않습니다.")
        );
    }

    public void modify(long id, String name, String color) {

        StationLine stationLine = findById(id);
        stationLine.updateName(name);
        stationLine.updateColor(color);

        repository.save(stationLine);
    }

    public void delete(long id) {
        repository.deleteById(id);
    }
}
