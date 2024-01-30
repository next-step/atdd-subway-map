package subway.line.delete;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.LineRepository;

@Transactional
@Service
public class LineDeleteService {

    private final LineRepository lineRepository;

    public LineDeleteService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public void deleteLine(Long lineId) {
        validateExistLine(lineId);
        lineRepository.deleteById(lineId);
    }

    private void validateExistLine(Long lineId) {
        if (!lineRepository.existsById(lineId)) {
            throw new IllegalArgumentException("존재하지 않는 노선입니다. lineId: " + lineId);
        }
    }
}
