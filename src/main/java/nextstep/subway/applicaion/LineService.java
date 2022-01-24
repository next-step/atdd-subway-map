package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.error.exception.EntityDuplicateException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        checkDuplicated(request.getName());

        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
        return LineResponse.of(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLine(Long id) {
        return LineResponse.of(findById(id));
    }

    public LineResponse updateLine(Long id, LineRequest lineRequest) {
        Line line = findById(id);
        line.edit(lineRequest.getName(), lineRequest.getColor());
        return LineResponse.of(line);
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    private Line findById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
    }

    private void checkDuplicated(String name) {
        lineRepository.findByName(name).ifPresent(l -> {
            throw new EntityDuplicateException();
        });
    }
}
