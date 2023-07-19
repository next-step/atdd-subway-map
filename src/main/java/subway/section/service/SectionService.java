package subway.section.service;

import org.springframework.stereotype.Service;
import subway.section.repository.Section;
import subway.section.repository.SectionRepository;

@Service
public class SectionService {
    private final SectionRepository sectionRepository;
    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public Section findSection(Long sectionId) {
        return sectionRepository.findById(sectionId)
                .orElseThrow(RuntimeException::new);
    }
}
