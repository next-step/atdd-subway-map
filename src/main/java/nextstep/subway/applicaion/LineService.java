package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
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
                Collections.EMPTY_LIST,
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

    @Transactional(readOnly = true)
    public LineResponse findLine(final Long id) {
        Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);

        return createLineResponse(line);
    }

    public void updateLine(final Long id, final LineRequest request) {
        Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        line.updateLine(request);
        lineRepository.save(line);
    }

    public void deleteLine(final Long id) {
        Line line = lineRepository.findById(id).orElseThrow(RuntimeException::new);
        lineRepository.delete(line);
    }

    private LineResponse createLineResponse(final Line line) {

        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                Collections.EMPTY_LIST,
                line.getCreatedDate(),
                line.getModifiedDate());
    }
}
