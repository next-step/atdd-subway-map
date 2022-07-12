package nextstep.subway.section.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.application.dto.LineResponse;
import nextstep.subway.line.domain.Line;
import nextstep.subway.section.application.dto.SectionRequest;
import nextstep.subway.section.domain.Section;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SectionService {

    private final LineService lineService;

    public SectionService(final LineService lineService) {
        this.lineService = lineService;
    }

    public LineResponse addSection(final long lineId, final SectionRequest sectionRequest) {
        final Line line = lineService.findLineById(lineId);
        final Section section = sectionRequest.toSection(lineId);
        if (!line.isConnectableSection(section)) {
            throw new IllegalArgumentException("신규상행역이 기존의 하행역이 아닙니다.");
        }

        return null;
    }

    public void deleteSection(final long lineId, final String stationId) {

    }

}
