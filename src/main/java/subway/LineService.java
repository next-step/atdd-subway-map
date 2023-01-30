package subway;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    public LineResponse saveLine(LineRequest lineRequest) {
        Line line = lineRepository.save(
                new Line(lineRequest.getName(), lineRequest.getUpStationId(), lineRequest.getDownStationId())
        );
        return createLineResponse(line);
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(line);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }
}
