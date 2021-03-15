package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.exception.LineUndefinedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Line persistLine = lineRepository.save(request.toLine());
        return LineResponse.of(persistLine);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> getLines() {
        return lineRepository.findAll()
                .stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse getLine(Long id) {
        final Line line = fineLineById(id);

        return LineResponse.of(line);
    }

    public void updateLine(Long id, LineRequest request) {
        final Line line = fineLineById(id);
        line.update(request.toLine());
    }

    public void deleteLine(Long id) {
        final Line line = fineLineById(id);
        lineRepository.delete(line);
    }

    private Line fineLineById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new LineUndefinedException(id));
    }
}
