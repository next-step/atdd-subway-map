package subway.dto.domain;

import lombok.Getter;
import subway.domain.sectionstation.SectionStation;

@Getter
public class CreateSectionStationVo {
    private SectionStation upSectionStation;
    private SectionStation downSectionStation;

    public CreateSectionStationVo(CreateSectionVo createSectionVo) {
        this.upSectionStation = createSectionVo.getUpSectionStation();
        this.downSectionStation = createSectionVo.getDownSectionStation();
    }
}
