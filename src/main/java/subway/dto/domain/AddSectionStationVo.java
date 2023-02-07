package subway.dto.domain;

import lombok.Getter;
import subway.domain.sectionstation.SectionStation;

@Getter
public class AddSectionStationVo {
    private SectionStation upSectionStation;
    private SectionStation downSectionStation;

    public AddSectionStationVo(AddSectionVo addSectionVo) {
        this.upSectionStation = addSectionVo.getUpSectionStation();
        this.downSectionStation = addSectionVo.getDownSectionStation();
    }
}
