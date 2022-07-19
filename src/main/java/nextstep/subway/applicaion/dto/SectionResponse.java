package nextstep.subway.applicaion.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nextstep.subway.domain.section.Section;
import nextstep.subway.utils.ObjectMapUtils;

@NoArgsConstructor
@Setter
@Getter
public class SectionResponse {

    private Long id;
    private Long lineId;
    private StationResponse upStation;
    private StationResponse downStation;
    private Long distance;

    public void addStation(Section lastSection) {
        this.upStation = ObjectMapUtils.map(lastSection.getUpStation(), StationResponse.class);
        this.downStation = ObjectMapUtils.map(lastSection.getDownStation(), StationResponse.class);
    }
}
