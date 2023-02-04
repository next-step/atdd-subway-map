package subway.application.service;

import org.springframework.stereotype.Component;
import subway.application.service.input.SectionLoadUseCase;
import subway.domain.Section;

@Component
public class SectionLoadService implements SectionLoadUseCase {

    @Override
    public Section loadSection(Long sectionId) {
        return null;
    }

}
