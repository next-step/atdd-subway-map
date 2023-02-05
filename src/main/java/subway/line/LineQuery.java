package subway.line;

import org.springframework.stereotype.Service;
import subway.exception.SubwayNotFoundException;

import java.util.List;

@Service
public class LineQuery {
    private LineRepository lineRepository;

    public LineQuery(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public List<Line> findAll() {
        return lineRepository.findAll();
    }

    public Line findById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new SubwayNotFoundException("Line not found with id, " + id));
    }
}
