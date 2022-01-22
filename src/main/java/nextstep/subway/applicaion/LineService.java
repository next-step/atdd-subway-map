package nextstep.subway.applicaion;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.applicaion.dto.LineRequest;
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
        if (isLineExists(line)) {
            throw new UniqueKeyExistsException(line.getName());
        }
        return lineRepository.save(new Line(line.getName(), line.getColor()));
    }

    private boolean isLineExists(Line line) {
        return lineRepository.findByName(line.getName()).isPresent();
    }

    public List<Line> getAllLines() {
        return lineRepository.findAll();
	}

    public Line getLine(long id) {
        return lineRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public Line updateLine(long id, LineRequest request) {
        final Line line = getLine(id);
        line.update(request.toEntity());
        return line;
    }

    @Transactional
    public void deleteLine(long id) {
        lineRepository.deleteById(id);
    }
}
