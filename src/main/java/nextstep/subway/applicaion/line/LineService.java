package nextstep.subway.applicaion.line;

import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineCreationRequest;
import nextstep.subway.applicaion.dto.LineModificationRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.exception.DomainException;
import nextstep.subway.domain.exception.DomainExceptionType;
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
                .orElseThrow(() -> new DomainException(DomainExceptionType.LINE_NOT_FOUND));

        line.setName(request.getName());
        line.setColor(request.getColor());

        return LineResponse.fromLine(line);
    }

    public void remove(Long lineId) {
        lineRepository.deleteById(lineId);
    }
}
