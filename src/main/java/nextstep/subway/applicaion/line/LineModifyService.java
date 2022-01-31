package nextstep.subway.applicaion.line;

import nextstep.subway.applicaion.dto.LineCreateResponse;
import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.domain.Section;
import nextstep.subway.exception.DuplicateCreationException;
import nextstep.subway.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LineModifyService {
    private final LineRepository lineRepository;

    public LineModifyService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public Line saveLine(String name, String color) {
        lineRepository
                .findByName(name)
                .ifPresent(
                        line -> {
                            throw new DuplicateCreationException();
                        });
        return lineRepository.save(new Line(name, color));
    }

    public void updateLine(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id).orElseThrow(NotFoundException::new);
        line.changeLineInformation(lineRequest.getName(), lineRequest.getColor());
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }
}
