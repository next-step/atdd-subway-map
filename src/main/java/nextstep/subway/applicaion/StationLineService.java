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

    private StationLineResponse createLineResponse(Line line) {
        return new StationLineResponse(
                line.getId(), line.getName(), line.getColor(),
                line.getUpStationId(), line.getDownStationId()
        );
    }
}
