package subway.domain.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.entity.Line;
import subway.domain.repository.LineRepository;

@Service
@RequiredArgsConstructor
public class LineCommander {

    private final LineRepository lineRepository;

    @Transactional
    public Long createLine(LineCommand.CreateLine command) {
        Line line = lineRepository.save(Line.init(command));
        return line.getId();
    }
}
