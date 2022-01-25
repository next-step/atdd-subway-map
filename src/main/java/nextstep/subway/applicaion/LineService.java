package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.entity.Line;
import nextstep.subway.domain.repository.LineRepository;
import nextstep.subway.domain.service.LineNameValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {

    private final LineRepository lineRepository;
    private final LineNameValidator lineNameValidator;

    public LineService(final LineRepository lineRepository, final LineNameValidator lineNameValidator) {
        this.lineRepository = lineRepository;
        this.lineNameValidator = lineNameValidator;
    }

    public LineResponse saveLine(final LineRequest request) {
        final Line line = lineRepository.save(new Line(
                request.getName(),
                request.getColor(),
                request.getUpStationId(),
                request.getDownStationId(),
                request.getDistance(),
                lineNameValidator
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
        line.changeName(lineRequest.getName(), lineNameValidator);
        line.changeColor(lineRequest.getColor());
    }

    public void deleteLineById(final Long id) {
        lineRepository.deleteById(id);
    }

    private LineResponse createLineResponse(final Line line) {
        return new LineResponse(
                line.getId(),
                line.getName(),
                line.getColor(),
                line.getUpStationId(),
                line.getDownStationId(),
                line.getDistance(),
                line.getCreatedDate(),
                line.getModifiedDate()
        );
    }
}
