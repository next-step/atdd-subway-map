package nextstep.subway.line.application;

import nextstep.subway.common.exception.ExistResourceException;
import nextstep.subway.common.exception.NonExistResourceException;
import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.common.exception.ExceptionMessage.EXCEPTION_MESSAGE_EXIST_LINE_NAME;
import static nextstep.subway.common.exception.ExceptionMessage.EXCEPTION_MESSAGE_NON_EXIST_LINE_NAME;

@Service
@Transactional
public class LineService {

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
            throw new ExistResourceException(EXCEPTION_MESSAGE_EXIST_LINE_NAME);
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
        Line updatedLine = lineRepository.save(line);

        return LineResponse.of(updatedLine);
    }

    public void deleteLine(Long id) {
        Line line = findLineById(id);

        lineRepository.delete(line);
    }

    private Line findLineById(Long id) {
        return lineRepository.findById(id)
                .orElseThrow(() -> new NonExistResourceException(EXCEPTION_MESSAGE_NON_EXIST_LINE_NAME));
    }
}
