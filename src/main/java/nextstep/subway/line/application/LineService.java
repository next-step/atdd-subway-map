package nextstep.subway.line.application;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineRepository;
import nextstep.subway.line.dto.LineRequest;
import nextstep.subway.line.dto.LineResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Line persistLine = lineRepository.save(request.toLine());
        return LineResponse.of(persistLine);
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

    private Line getLineEntity(Long lineId) {
        if (Objects.isNull(lineId)) {
            throw new IllegalArgumentException("지하철 노선 ID를 입력해주세요.");
        }
        return lineRepository.findById(lineId)
                .orElseThrow(EntityNotFoundException::new);
    }

}
