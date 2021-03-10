package nextstep.subway.line.domain;

import nextstep.subway.line.exception.LineNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Transactional
@Service
public class LineDomainService {

    private LineRepository lineRepository;

    public LineDomainService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public Line getLineEntity(Long lineId) {
        if (Objects.isNull(lineId)) {
            throw new IllegalArgumentException("지하철 노선 ID를 입력해주세요.");
        }
        return lineRepository.findById(lineId)
                .orElseThrow(() -> new LineNotFoundException(lineId));
    }

}
