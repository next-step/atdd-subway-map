package subway.application.service;

import org.springframework.stereotype.Component;
import subway.application.service.input.SectionLoadUseCase;
import subway.application.service.output.SectionLoadRepository;
import subway.domain.Section;

@Component
public class SectionLoadService implements SectionLoadUseCase {

    private final SectionLoadRepository sectionLoadRepository;

    public SectionLoadService(SectionLoadRepository sectionLoadRepository) {
        this.sectionLoadRepository = sectionLoadRepository;
    }

    @Override
    public Section loadSection(Long sectionId) {
        return sectionLoadRepository.loadSection(sectionId);
    }

}
