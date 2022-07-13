package nextstep.subway.applicaion.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import nextstep.subway.domain.Line;
import nextstep.subway.domain.Section;
import nextstep.subway.domain.Station;

@Getter
@Setter
@ToString
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

    public static SectionResponse of(Section section) {
        return new SectionResponse(
                section.getId(),
                section.getLine().getId(),
                section.getUpStation(),
                section.getDownStation(),
                section.getDistance()
        );
    }
}
