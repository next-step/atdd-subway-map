package subway.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.contract.LineCommand;
import subway.domain.entity.Line;
import subway.domain.repository.LineRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LineService {

    private final LineRepository lineRepository;

    @Transactional
    public Long createLine(LineCommand.CreateLine command) {
        Line line = lineRepository.save(Line.createLine(command));
        return line.getId();
    }

    @Transactional(readOnly = true)
    public List<Line> getLines() {
        return lineRepository.findAll();
    }
}
