package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.StationLineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.StationLineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class StationLineService {

    private final StationLineRepository repository;

    public StationLineService(StationLineRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public StationLineResponse save(Line line) {
        return createLineResponse(repository.save(line));
    }

    public List<StationLineResponse> findAllStationLines() {
        return repository.findAll()
                         .stream()
                         .map(this::createLineResponse)
                         .toList();
    }

    public StationLineResponse findById(Long id) {
        return createLineResponse(repository.getById(id));
    }

    public StationLineResponse patch(Long id, Line line) {
        Line previousLine = repository.getById(id);
        Line currentLine = new Line(previousLine.getId(), line.getName(), line.getColor(), line.getUpStationId(), line.getDownStationId());
        return createLineResponse(repository.save(currentLine));
    }

    private StationLineResponse createLineResponse(Line line) {
        return new StationLineResponse(
                line.getId(), line.getName(), line.getColor(),
                line.getUpStationId(), line.getDownStationId()
        );
    }

    @Transactional
    public void deleteLineById(Long id) {
        repository.deleteById(id);
    }
}
