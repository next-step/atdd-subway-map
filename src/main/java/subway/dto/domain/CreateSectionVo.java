package subway.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.domain.section.Section;
import subway.domain.sectionstation.SectionStation;

@Getter
@AllArgsConstructor
public class CreateSectionVo {
    private Section section;
    private SectionStation upSectionStation;
    private SectionStation downSectionStation;
}
