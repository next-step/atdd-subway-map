package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Line persistLine = lineRepository.save(request.toLine());
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> getLineList() {
        List<Line> lineList = lineRepository.findAll();
        return lineList.stream()
                .map(line -> LineResponse.of(line))
                .collect(Collectors.toList());
    }

    public LineResponse getLine(Long id) {
        return Optional.ofNullable(lineRepository.findById(id).get())
                .map(line -> LineResponse.of(line))
                .get();
    }

    public LineResponse updateLine(Line line) {
        Line addedLine = lineRepository.findById(line.getId()).orElse(new Line());
        addedLine.update(line);
        return LineResponse.of(lineRepository.save(addedLine));
    }

    public void deleteLine(Long id) {
        lineRepository.deleteById(id);
    }
}
