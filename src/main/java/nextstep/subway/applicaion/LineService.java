package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    private LineResponse createLineResponse(Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }

    @Transactional(readOnly = true)
    public LineResponse findLine(Long id) {
        Optional<Line> line = lineRepository.findById(id);
        return createLineResponse(line.orElseThrow(() -> new NoSuchElementException()));
    }

    public LineResponse updateLine(Long id, LineRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
        line.updateLine(request.getName(), request.getColor());
        return createLineResponse(lineRepository.save(line));
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public boolean isDuplicatedNameOfLine(String name) {
        Optional<Line> line = lineRepository.findByName(name);
        return !line.isEmpty();
    }
}
