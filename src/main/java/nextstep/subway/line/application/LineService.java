package nextstep.subway.line.application;

import nextstep.subway.exceptions.NotFoundLineException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Line persistLine = lineRepository.save(request.toLine());
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> readLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream().map(LineResponse::of).collect(Collectors.toList());
    }

    public LineResponse readLine(Long id) {
        Line line = findById(id);
        return LineResponse.of(line);
    }

    @Modifying
    public void updateLine(Long id, Line target) {
        Line line = findById(id);
        line.update(target);
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }

    private Line findById(Long id) {
        return lineRepository.findById(id).orElseThrow(() -> new NotFoundLineException(id));
    }
}
