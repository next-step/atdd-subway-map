package subway.infrastructure.line.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import subway.domain.line.entity.Line;
import subway.infrastructure.line.LineRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LineReader {
    private final LineRepository lineRepository;

    public Line readBy(Long id) {
        return lineRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    public List<Line> listAll() {
        return lineRepository.findAll();
    }
}
