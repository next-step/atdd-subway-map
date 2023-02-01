package subway.line.domain;

import org.springframework.stereotype.Repository;
import subway.line.exception.LineNotFoundException;

import java.util.List;

@Repository
public class LineQuery {
    private final LineRepository lineRepository;

    public LineQuery(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public List<Line> findAll() {
        return lineRepository.findAll();
    }

    public Line findById(final Long lineId) {
        return lineRepository.findById(lineId)
                .orElseThrow(LineNotFoundException::new);
    }
}
