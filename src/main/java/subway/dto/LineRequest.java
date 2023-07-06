package subway.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.Line;
import subway.domain.Station;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class LineRequest {

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Long distance;

    public Line toEntity(Station upStation, Station downStation) {
        return new Line(
                this.name,
                this.color,
                upStation,
                downStation,
                this.distance
        );
    }

}
