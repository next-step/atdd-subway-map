package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.exception.LineNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {
    private LineRepository lineRepository;

    public LineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineResponse saveLine(final LineRequest request) {
        Line persistLine = lineRepository.save(request.toLine());
        return LineResponse.of(persistLine);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        List<Line> lines = lineRepository.findAll();

        return lines.stream()
                .map(line -> LineResponse.of(line))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse getLine(final Long id) {
        return lineRepository.findById(id)
                .map(l -> LineResponse.of(l))
                .orElseThrow(() -> new LineNotFoundException());
    }

    @Transactional
    public void updateLine(final Long id, final LineRequest lineRequest) {
        Line line = lineRepository.findById(id).orElseThrow(() -> new LineNotFoundException());
        line.update(lineRequest.toLine());
    }

    @Transactional
    public void deleteLine(final Long id) {
        lineRepository.deleteById(id);
    }
}
