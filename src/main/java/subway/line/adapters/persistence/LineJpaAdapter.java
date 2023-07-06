package subway.line.adapters.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.global.error.code.ErrorCode;
import subway.global.error.exception.NotEntityFoundException;
import subway.line.entity.Line;
import subway.line.repository.LineRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LineJpaAdapter {

    private final LineRepository lineRepository;

    @Transactional
    public Line save(Line line) {
        return lineRepository.save(line);
    }

    public Line findById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new NotEntityFoundException(ErrorCode.NOT_EXIST_LINE));
    }

    public List<Line> findAll() {
        return lineRepository.findAll();
    }

    public void deleteById(Long id) {
        lineRepository.deleteById(id);
    }
}
