package subway.line.domain;

import org.springframework.stereotype.Repository;

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
}
