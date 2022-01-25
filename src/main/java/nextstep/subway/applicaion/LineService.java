package nextstep.subway.applicaion;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;

@Service
@Transactional
public class LineService {
    private final String NOT_EXISTS_NOTION = "해당 노선에 대한 정보가 없습니다.";

    private LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Line line = lineRepository.save(new Line(request.getName(), request.getColor()));
        return LineResponse.of(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
            .map(line -> LineResponse.of(line))
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(Long id) {
        Line line = lineRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTS_NOTION));
        return LineResponse.of(line);
    }

    public void updateLineById(Long id, LineRequest lineRequest) {
        Line line = lineRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(NOT_EXISTS_NOTION));

        line.update(lineRequest.getName(), lineRequest.getColor());
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }
}
