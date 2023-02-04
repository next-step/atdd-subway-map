package subway.application.service.input;

import subway.domain.Section;

public interface SectionLoadUseCase {

    Section loadSection(Long sectionId);

}
