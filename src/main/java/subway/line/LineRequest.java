package subway.line;

import lombok.AllArgsConstructor;
import lombok.Getter;
import subway.station.Station;

@Getter
@AllArgsConstructor
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
