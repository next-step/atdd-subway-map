package nextstep.subway.applicaion.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.Station;

@Getter
@NoArgsConstructor
public class SectionResponse {
    private Long id;
    private Long lineId;
    public Station upStation;
    public Station downStation;
    private Integer distance;

    public SectionResponse(Long id, Long lineId, Station upStation, Station downStation, Integer distance) {
        this.id = id;
        this.lineId = lineId;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }
}
