package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import nextstep.subway.exception.DuplicateException;
import nextstep.subway.exception.NotFoundLineException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(final LineRequest request) {
        final String lineName = request.getName();
        if (isDuplicate(lineName)) {
            throw new DuplicateException();
        }
        final Line line = new Line(lineName, request.getColor());
        final Line createdLine = lineRepository.save(line);
        return LineResponse.of(createdLine);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        final List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(final Long id) {
        return LineResponse.of(getLineById(id));
    }

    public void updateLine(final Long id, final LineRequest request) {
        Line line = getLineById(id);
        line.update(request.getName(), request.getColor());
    }

    public void deleteLineById(final Long id) {
        lineRepository.deleteById(id);
    }

    private Line getLineById(final Long id) {
        return lineRepository.findById(id)
                .orElseThrow(NotFoundLineException::new);
    }

    private boolean isDuplicate(final String lineName) {
        final Optional<Line> station = lineRepository.findByName(lineName);
        return station.isPresent();
    }
}
