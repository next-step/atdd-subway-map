package nextstep.subway.applicaion.line;

import java.util.List;
import java.util.stream.Collectors;
import nextstep.subway.applicaion.dto.LineResponse;
import nextstep.subway.domain.exception.DomainException;
import nextstep.subway.domain.exception.DomainExceptionType;
import nextstep.subway.domain.line.LineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class LineQueryService {

    private final LineRepository lineRepository;

    public LineQueryService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public List<LineResponse> getAllLines() {
        return lineRepository.findAll()
                .stream()
                .map(LineResponse::fromLine)
                .collect(Collectors.toList());
    }

    public LineResponse getLineById(Long id) {
        return lineRepository.findById(id)
                .map(LineResponse::fromLine)
                .orElseThrow(() -> new DomainException(DomainExceptionType.LINE_NOT_FOUND));
    }
}
