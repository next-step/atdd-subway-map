package subway.domain.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.LineCommand;
import subway.domain.line.entity.Line;
import subway.domain.line.entity.Section;
import subway.infrastructure.line.dao.LineReader;
import subway.infrastructure.line.dao.LineStore;
import subway.interfaces.line.dto.LinePatchRequest;
import subway.interfaces.line.dto.LineRequest;
import subway.interfaces.line.dto.LineResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class SectionService {

    private final LineStore lineStore;
    private final LineReader lineReader;

    public SectionService(LineStore lineStore, LineReader lineReader) {
        this.lineStore = lineStore;
        this.lineReader = lineReader;
    }
    @Transactional
    public LineResponse saveSection(LineCommand.SectionAddCommand command) {
        Section section = lineStore.createSection(command);
        Line line = lineReader.readBy(command.getLineId());
        line.add(section);
        return LineResponse.from(line);
    }
}
