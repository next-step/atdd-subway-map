package subway.line;

import org.springframework.stereotype.Service;

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
        return lineRepository.findById(id).get(); // TODO NullException 추가
    }
}
