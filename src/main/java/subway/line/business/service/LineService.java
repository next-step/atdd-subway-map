package subway.line.business.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.business.constant.LineConstants;
import subway.line.business.model.Line;
import subway.line.repository.LineRepository;
import subway.line.repository.entity.LineEntity;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new NoSuchElementException(LineConstants.LINE_NOT_EXIST))
                .toLine();
    }

    @Transactional
    public void modify(Long id, String name, String color) {
        LineEntity entity = new LineEntity(getLine(id));
        lineRepository.save(entity.modify(name, color));
    }

    @Transactional
    public void remove(Long id) {
        lineRepository.deleteById(id);
    }

}
