package nextstep.subway.application;

import nextstep.subway.application.dto.SectionRequest;
import nextstep.subway.application.dto.SectionResponse;
import nextstep.subway.domain.*;
import nextstep.subway.domain.exception.LineException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SectionService {
    private final LineRepository lineRepository;

    public SectionService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public SectionResponse saveSection(Long lineId, SectionRequest sectionRequest) {
        Line line = lineRepository.findById(lineId).orElseThrow(() -> new LineException.NotFound(lineId));

        return new SectionResponse();
    }
}
