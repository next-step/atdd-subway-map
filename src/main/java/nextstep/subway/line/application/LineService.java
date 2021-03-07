package nextstep.subway.line.application;

import nextstep.subway.exception.SubwayNameDuplicateException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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
        boolean isExistSubwayName = lineRepository.existsByName(request.getName());
        if (isExistSubwayName) {
            throw new SubwayNameDuplicateException();
        }

        Line persistLine = lineRepository.save(request.toLine());
        return LineResponse.of(persistLine);
    }

    public List<LineResponse> getAll() {
        List<Line> lines = lineRepository.findAll();
        if (lines.isEmpty()) {
            return Collections.emptyList();
        }

        return lines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    private Line getLine(long id) {
        return Optional.of(lineRepository.findById(id))
                .orElseThrow(NullPointerException::new)
                .get();
    }

    public LineResponse get(long id) {
        return LineResponse.of(getLine(id));
    }

    public LineResponse saveLine(Long id, LineRequest lineRequest) {
        Line line = getLine(id);
        line.update(lineRequest.toLine());

        lineRepository.save(line);
        return LineResponse.of(line);
    }

    public void delete(long id) {
        Line line = getLine(id);
        lineRepository.delete(line);
    }
}
