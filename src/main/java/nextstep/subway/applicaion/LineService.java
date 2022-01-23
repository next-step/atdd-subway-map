package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@Service
public class LineService {

    private final LineRepository lineRepository;
    private static final String NOT_FOUND_LINE = "노선을 찾을 수 없습니다.";

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Line line = new Line(request.getName(), request.getColor());
        Line createdLine = lineRepository.save(line);
        return LineResponse.of(createdLine);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(Long id) {
        return LineResponse.of(getLineById(id));
    }

    public void updateLine(Long id, LineRequest request) {
        Line line = getLineById(id);
        line.update(request.getName(), request.getColor());
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }

    private Line getLineById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(NOT_FOUND_LINE));
    }
}
