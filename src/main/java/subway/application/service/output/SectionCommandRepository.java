package subway.application.service.output;

import subway.domain.Section;

public interface SectionCommandRepository {

    Long createSection(Section section);

    void deleteSection(Section section);

}
