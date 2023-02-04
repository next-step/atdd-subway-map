package subway.application.service.output;

import subway.domain.Section;

public interface SectionLoadRepository {

    Section loadSection(Long sectionId);

}
