package subway.line.removeSection;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.section.Section;
import subway.section.SectionRepository;
import subway.section.Sections;

@Transactional
@Service
public class LineRemoveSectionService {

    private final SectionRepository sectionRepository;

    public LineRemoveSectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }


    public void deleteSection(Long lineId, Long stationdId) {
        Sections sections = findSectionsByLineId(lineId);
        sections.validateRemovableLastSection(stationdId);
        Section lastSection = sections.getLastSection();
        sectionRepository.deleteById(lastSection.getId());
    }

    private Sections findSectionsByLineId(Long lineId) {
        return new Sections(sectionRepository.findAllByLineIdOrderById(lineId));
    }
}
