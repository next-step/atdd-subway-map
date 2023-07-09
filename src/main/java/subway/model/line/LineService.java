package subway.model.line;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public Line save(Line newLine) {
        return lineRepository.save(newLine);
    }

    public Line findById(Long lineId) {
        return lineRepository.findById(lineId)
                             .orElseThrow(() -> new IllegalArgumentException("line id doesn't exist"));
    }

    public List<Line> findAll() {
        return lineRepository.findAll();
    }

    public void deleteById(Long lineId) {
        lineRepository.deleteById(lineId);
    }
}
