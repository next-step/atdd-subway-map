package nextstep.subway.line.application;

import nextstep.subway.line.NotFoundLineException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
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

    public LineResponse findById(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundLineException(id));
        return LineResponse.of(line);
    }

    public void update(Long id, LineRequest updateRequest) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundLineException(id));
        line.update(updateRequest.toLine());
    }

    public void deleteById(Long id) {
        lineRepository.findById(id)
                .orElseThrow(() -> new NotFoundLineException(id));
        lineRepository.deleteById(id);
    }

    public List<LineResponse> findAll() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
                .map(line -> LineResponse.of(line))
                .collect(Collectors.toList());
    }
}
