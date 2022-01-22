package nextstep.subway.applicaion;

import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public Line saveLine(Line request) {
        return lineRepository.save(new Line(request.getName(), request.getColor()));
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
