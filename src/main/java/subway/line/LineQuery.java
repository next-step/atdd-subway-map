package subway.line;

import org.springframework.stereotype.Component;
import subway.exception.LineNotFoundException;

import java.util.List;

@Component
public class LineQuery {
    private LineRepository lineRepository;

    public LineQuery(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public List<Line> findAll() {
        return lineRepository.findAll();
    }

    public Line findById(long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new LineNotFoundException(lineId));
    }
}
