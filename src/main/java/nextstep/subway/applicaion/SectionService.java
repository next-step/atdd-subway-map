package nextstep.subway.applicaion;

import java.util.List;
import java.util.stream.Collectors;
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

    public List<SectionResponse> getSections(long lineId) {
        List<Section> sections = sectionRepository.findSectionsByLineId(lineId);
        return sections.stream().map(SectionResponse::convertedByEntity).collect(Collectors.toList());
    }
}
