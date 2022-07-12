package nextstep.subway.section.application;

import nextstep.subway.line.application.LineService;
import nextstep.subway.line.application.dto.LineResponse;
import nextstep.subway.line.domain.Line;
import nextstep.subway.section.application.dto.SectionRequest;
import nextstep.subway.section.application.dto.SectionResponse;
import nextstep.subway.section.domain.Section;
import nextstep.subway.section.domain.SectionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class SectionService {

    private final LineService lineService;
    private final SectionRepository sectionRepository;

    public SectionService(final LineService lineService, final SectionRepository sectionRepository) {
        this.lineService = lineService;
        this.sectionRepository = sectionRepository;
    }

    public SectionResponse addSection(final long lineId, final SectionRequest sectionRequest) {
        final Line line = lineService.findLineById(lineId);
        final Section section = sectionRequest.toSection(lineId);

        if (!line.isConnectableSection(section)) {
            throw new IllegalArgumentException("신규상행역이 기존의 하행역이 아닙니다.");
        }

        if (hasCircularSection(lineId, section)) {
            throw new IllegalArgumentException("신규하행역이 이미 등록되어 있습니다.");
        }

        return createSectionResponse(sectionRepository.save(section));
    }

    private SectionResponse createSectionResponse(final Section savedSection) {
        return SectionResponse.builder()
                .id(savedSection.getId())
                .upStationId(savedSection.getUpStationId())
                .downStationId(savedSection.getDownStationId())
                .distance(savedSection.getDistance())
                .build();
    }

    private boolean hasCircularSection(final long lineId, final Section section) {
        return sectionRepository.findAllByLineId(lineId)
                .stream()
                .anyMatch(section::isDuplicated);
    }

    public void deleteSection(final long lineId, final String stationId) {

    }

}
