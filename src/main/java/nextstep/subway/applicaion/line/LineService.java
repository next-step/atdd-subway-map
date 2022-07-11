package nextstep.subway.applicaion.line;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineCreationRequest;
import nextstep.subway.applicaion.dto.LineModificationRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.line.Line;
import nextstep.subway.domain.line.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LineService {

    private final LineRepository lineRepository;

    public LineResponse create(LineCreationRequest request) {
        var line = new Line(
                null,
                request.getName(),
                request.getColor(),
                request.getUpStationId(),
                request.getDownStationId(),
                request.getDistance());

        return LineResponse.fromLine(lineRepository.save(line));
    }

    public LineResponse modify(Long lineId, LineModificationRequest request) {
        var line = lineRepository.findById(lineId)
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 노선 ID 입니다."));

        line.changeNameAndColor(request.getName(), request.getColor());

        return LineResponse.fromLine(line);
    }

    public void remove(Long lineId) {
        lineRepository.deleteById(lineId);
    }
}
