package nextstep.subway.applicaion.line;

import nextstep.subway.applicaion.dto.LineModificationRequest;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.exception.DomainException;
import nextstep.subway.domain.exception.DomainExceptionType;
import nextstep.subway.domain.line.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LineModifier {

    private final LineRepository lineRepository;

    public LineModifier(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    @Transactional
    public LineResponse modify(Long lineId, LineModificationRequest request) {
        var line = lineRepository.findById(lineId)
                .orElseThrow(() -> new DomainException(DomainExceptionType.LINE_NOT_FOUND));

        line.setName(request.getName());
        line.setColor(request.getColor());

        return LineResponse.fromLine(line);
    }
}
