package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.exception.LineAlreadyExistException;
import nextstep.subway.line.exception.LineNonExistException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {

    private static final String EXCEPTION_MESSAGE_EXIST_LINE_NAME = "존재하는 지하철 노선 입니다.";
    private static final String EXCEPTION_MESSAGE_NON_EXIST_LINE_NAME = "존재하지 않는 지하철 노선입니다.";

    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        validateLineName(request.getName());

        Line savedLine = lineRepository.save(request.toLine());
        return LineResponse.of(savedLine);
    }

    private void validateLineName(String lineName) {
        if (lineRepository.existsByName(lineName)) {
            throw new LineAlreadyExistException(EXCEPTION_MESSAGE_EXIST_LINE_NAME);
        }
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLine(Long id) {
        Line line = findLineById(id);
        return LineResponse.of(line);
    }

    public LineResponse updateLine(Long id, LineRequest lineRequest) {
        Line line = findLineById(id);

        line.update(lineRequest.toLine());

        return LineResponse.of(line);
    }

    public void deleteLine(Long id) {
        Line line = findLineById(id);

        lineRepository.delete(line);
    }

    private Line findLineById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new LineNonExistException(EXCEPTION_MESSAGE_NON_EXIST_LINE_NAME));
    }
}
