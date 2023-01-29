package subway.line.business.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import subway.line.business.model.Line;
import subway.line.repository.LineRepository;
import subway.line.repository.entity.LineEntity;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LineService {

    private final LineRepository lineRepository;

    public Line create(Line line) {
        return lineRepository.save(new LineEntity(line)).toLine();
    }

    public List<Line> getAllLines() {
        return lineRepository.findAll().stream().map(LineEntity::toLine).collect(Collectors.toList());
    }

    public Line getLine(Long lineId) {
        return lineRepository.findById(lineId).get().toLine();
    }

}
