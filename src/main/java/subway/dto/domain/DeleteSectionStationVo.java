package subway.dto.domain;

import lombok.Getter;
import subway.domain.sectionstation.SectionStation;

@Getter
public class DeleteSectionStationVo {
    private SectionStation upSectionStation;
    private SectionStation downSectionStation;

    public DeleteSectionStationVo(DeleteSectionVo deleteSectionVo) {
        this.upSectionStation = deleteSectionVo.getUpSectionStation();
        this.downSectionStation = deleteSectionVo.getDownSectionStation();
    }
}
