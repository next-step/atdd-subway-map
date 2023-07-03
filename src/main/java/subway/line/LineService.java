package subway.line;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineResponse saveLine(Line line) {
        return LineResponse.from(lineRepository.save(line));
    }

    public List<LineResponse> showLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::from)
                .collect(Collectors.toList());
    }

    public LineResponse searchById(Long id) {
        return LineResponse.from(lineRepository.findById(id).orElseThrow());
    }

    @Transactional
    public void update(Long id, UpdateLineRequest updateLineRequest) {
        Line line = lineRepository.findById(id).orElseThrow();
        line.update(updateLineRequest.getName(), updateLineRequest.getColor());
    }
}
