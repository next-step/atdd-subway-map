package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import nextstep.subway.line.exception.LineCreateFailException;
import nextstep.subway.line.exception.LineNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.OperationNotSupportedException;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class LineService {

    private LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Line line = request.toLine();
        boolean alreadyRegistered = lineRepository.findByName(line.getName()).isPresent();
        if (alreadyRegistered) {
            throw LineCreateFailException.alreadyExist();
        }
        return LineResponse.of(lineRepository.save(line));
    }

    public List<LineResponse> getAllLines() {
        return lineRepository.findAll().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse getLine(Long lineId) {
        return LineResponse.of(getLineEntity(lineId));
    }

    public void updateLine(Long lineId, LineRequest lineRequest) {
        Line line = getLineEntity(lineId);
        line.update(lineRequest.toLine());
    }

    public void deleteLine(Long lineId) {
        Line line = getLineEntity(lineId);
        lineRepository.delete(line);
    }

    public Line getLineEntity(Long lineId) {
        if (Objects.isNull(lineId)) {
            throw new IllegalArgumentException("지하철 노선 ID를 입력해주세요.");
        }
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new LineNotFoundException(lineId));
    }


}
