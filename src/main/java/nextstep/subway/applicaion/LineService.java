package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.entity.Line;
import nextstep.subway.domain.repository.LineRepository;
import nextstep.subway.domain.service.LineValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {

    private final LineRepository lineRepository;
    private final LineValidator lineValidator;

    public LineService(final LineRepository lineRepository, final LineValidator lineValidator) {
        this.lineRepository = lineRepository;
        this.lineValidator = lineValidator;
    }

    public LineResponse saveLine(final LineRequest request) {
        final Line line = lineRepository.save(new Line(
                request.getName(),
                request.getColor(),
                lineValidator
        ));

        return createLineResponse(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        final List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(this::createLineResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(final Long id) {
        final Line line = lineRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        return createLineResponse(line);
    }

    public void updateLine(final Long id, final LineRequest lineRequest) {
        final Line line = lineRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        line.change(lineRequest.getName(), lineRequest.getColor(), lineValidator);
    }

    public void deleteLineById(final Long id) {
        lineRepository.deleteById(id);
    }

    private LineResponse createLineResponse(final Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }
}
