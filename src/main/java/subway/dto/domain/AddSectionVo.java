package subway.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.domain.section.Section;
import subway.domain.sectionstation.SectionStation;

@Getter
@AllArgsConstructor
public class AddSectionVo {
    private Section section;
    private SectionStation upSectionStation;
    private SectionStation downSectionStation;
}
