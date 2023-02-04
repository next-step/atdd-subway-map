package subway.application.service.input;

import subway.domain.Section;

import java.util.List;

public interface SectionLoadUseCase {

    Section loadSection(Long sectionId);

    List<Section> loadLineSection(Long loadLineId);

}
