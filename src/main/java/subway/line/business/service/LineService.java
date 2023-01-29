package subway.line.business.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.business.model.Line;
import subway.line.repository.LineRepository;
import subway.line.repository.entity.LineEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LineService {

    private final LineRepository lineRepository;

    @Transactional
    public Line create(Line line) {
        return lineRepository.save(new LineEntity(line)).toLine();
    }

    public List<Line> getAllLines() {
        return lineRepository.findAll().stream().map(LineEntity::toLine).collect(Collectors.toList());
    }

    public Line getLine(Long lineId) {
        LineEntity entity = lineRepository.findById(lineId).orElse(null);
        return entity != null ? entity.toLine() : null;
    }

    @Transactional
    public void modify(Long id, String name, String color) {
        LineEntity entity = lineRepository.findById(id).get();
        lineRepository.save(entity.modify(name, color));
    }

    @Transactional
    public void remove(Long id) {
        lineRepository.deleteById(id);
    }

}
