package nextstep.subway.applicaion;

import nextstep.subway.applicaion.dto.SectionRequest;
import nextstep.subway.applicaion.dto.SectionResponse;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.SectionRepository;
import org.springframework.stereotype.Service;

@Service
public class SectionService {
    private final SectionRepository sectionRepository;

    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public SectionResponse saveSection(SectionRequest sectionRequest, Long lineId) {
        final Section section = sectionRepository.save(new Section(lineId,
                sectionRequest.getDownStationId(),
                sectionRequest.getUpStationId(),
                sectionRequest.getDistance()));

        return new SectionResponse(section.getId(),
                section.getLineId(),
                section.getDownStationId(),
                section.getUpStationId(),
                section.getDistance(),
                section.getCreatedDate(),
                section.getModifiedDate()
        );
    }
}
