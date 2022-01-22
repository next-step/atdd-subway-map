package nextstep.subway.applicaion;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.ui.exception.UniqueKeyExistsException;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public Line saveLine(Line line) {
        validateCreateLine(line);
        return lineRepository.save(new Line(line.getName(), line.getColor()));
    }

    public List<Line> getAllLines() {
        return lineRepository.findAll();
	}

    public Line getLine(long id) {
        return lineRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public Line updateLine(long id, Line request) {
        final Line line = getLine(id);
        line.update(request);

        validateUpdateLine(line);

        return line;
    }

    @Transactional
    public void deleteLine(long id) {
        lineRepository.deleteById(id);
    }

    private void validateUpdateLine(Line line) {
        if (lineRepository.existsByNameAndIdIsNot(line.getName(), line.getId())) {
            throw new UniqueKeyExistsException(line.getName());
        }
    }

    private void validateCreateLine(Line line) {
        if (lineRepository.existsByName(line.getName())) {
            throw new UniqueKeyExistsException(line.getName());
        }
    }
}
