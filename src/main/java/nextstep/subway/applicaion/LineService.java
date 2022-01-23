package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.LineRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

import static nextstep.subway.common.ErrorMessages.DUPLICATE_LINE_NAME;
import static nextstep.subway.common.ErrorMessages.NOT_FOUND_LINE;

@Service
@Transactional
public class LineService {
    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        checkExistsLineName(request.getName());

        Line line = lineRepository.save(request.toLine());
        return LineResponse.of(line);
    }

    @Transactional(readOnly = true)
    public List<LineResponse> findAllLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::of).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LineResponse findLineById(Long id) {
        Line line = getLine(id);
        return LineResponse.of(line);
    }

    public void updateLine(Long id, LineRequest updateRequest) {
        checkExistsLineName(updateRequest.getName());

        Line line = getLine(id);
        line.update(updateRequest);
    }

    public void deleteLineById(Long id) {
        getLine(id);

        lineRepository.deleteById(id);
    }

    private Line getLine(Long id) {
        Line line = lineRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(NOT_FOUND_LINE.getMessage()));
        return line;
    }

    private void checkExistsLineName(String name) {
        Assert.isTrue(!lineRepository.existsByName(name), DUPLICATE_LINE_NAME.getMessage());
    }
}
