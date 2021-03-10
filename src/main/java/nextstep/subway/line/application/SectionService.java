package nextstep.subway.line.application;

import nextstep.subway.line.domain.Section;
import nextstep.subway.line.domain.SectionRepository;
import nextstep.subway.line.dto.SectionRequest;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional
public class SectionService {

    private SectionRepository sectionRepository;

    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public Section createSection(Long lineId, SectionRequest sectionRequest) {
        Section section = sectionRequest.toSection();

        //line 할당
        section.assignLine(lineId);
        return sectionRepository.save(section);
    }

    @Transactional(readOnly = true)
    public List<Section> getSectionsByLineId(Long lineId) {
        return sectionRepository.findAllByLineId(lineId);
    }
}
