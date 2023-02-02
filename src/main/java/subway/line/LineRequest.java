package subway.line;

import lombok.Builder;
import lombok.Getter;
import subway.station.Station;

@Getter
@Builder
public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Long distance;

    public Line toEntity(Station upStation, Station downStation) {
        return new Line(this.name, this.color, upStation, downStation, this.distance);
    }
}
