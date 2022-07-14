package nextstep.subway.applicaion;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.StationLineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.StationLineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StationLineService {

    private final StationLineRepository repository;

    @Transactional
    public StationLineResponse save(Line line) {
        return createLineResponse(repository.save(line));
    }

    public List<StationLineResponse> findAllStationLines() {
        return repository.findAll()
                         .stream()
                         .map(this::createLineResponse)
                         .collect(Collectors.toList());
    }

    public StationLineResponse findById(Long id) {
        return createLineResponse(repository.getById(id));
    }

    @Transactional
    public void update(Line line) {
        createLineResponse(repository.save(line));
    }

    private StationLineResponse createLineResponse(Line line) {
        return new StationLineResponse(
                line.getId(), line.getName(), line.getColor(),
                line.getUpStationId(), line.getDownStationId(), line.getDistance()
        );
    }

    @Transactional
    public void deleteLineById(Long id) {
        repository.deleteById(id);
    }
}
