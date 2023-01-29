package subway.line;

import org.springframework.stereotype.Component;
import subway.exception.LineNotFoundException;

import java.util.List;
import java.util.Optional;

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
        Optional<Line> OptionalLine = lineRepository.findById(lineId);
        if (OptionalLine.isEmpty()) {
            throw new LineNotFoundException(lineId);
        }
        return OptionalLine.get();
    }
}
