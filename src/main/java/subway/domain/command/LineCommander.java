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

    @Transactional
    public void updateLine(LineCommand.UpdateLine command) {
        Line line = lineRepository.findById(command.getId()).orElseThrow(() -> new RuntimeException("Not Found line"));
        line.update(command);
        lineRepository.save(line);
    }

    @Transactional
    public void deleteLineById(Long id) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new RuntimeException("Not Found line"));
        lineRepository.delete(line);
    }
}
