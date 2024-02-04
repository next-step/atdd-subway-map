package subway.domain.line.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.entity.Line;
import subway.domain.line.entity.Section;
import subway.infrastructure.line.dao.LineReader;
import subway.infrastructure.line.dao.LineStore;
import subway.interfaces.line.dto.LinePatchRequest;
import subway.interfaces.line.dto.LineRequest;
import subway.interfaces.line.dto.LineResponse;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineStore lineStore;
    private final LineReader lineReader;

    public LineService(LineStore lineStore, LineReader lineReader) {
        this.lineStore = lineStore;
        this.lineReader = lineReader;
    }

    @Transactional
    public LineResponse saveLine(LineRequest.Line request) {
        Section section = lineStore.createSection(request);
        Line init = new Line(request.getName(), request.getColor(), section);
        return LineResponse.from(lineStore.store(init));
    }

    @Transactional(readOnly = true)
    public LineResponse retrieveBy(Long id) {
        Line line = lineReader.readBy(id);
        return LineResponse.from(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> listAll() {
        return lineReader.listAll().stream().map(LineResponse::from).collect(Collectors.toList());
    }

    @Transactional
    public void updateBy(Long id, LinePatchRequest request) {
        Line line = lineReader.readBy(id);
        line.changeName(request.getName());
        line.changeColor(request.getColor());
    }

    @Transactional
    public void deleteBy(Long id) {
        Line line = lineReader.readBy(id);
        lineStore.remove(line);
    }
}
