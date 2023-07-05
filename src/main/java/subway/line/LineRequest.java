package subway.line;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import subway.station.Station;

@Data
@Getter
@AllArgsConstructor
public class LineRequest {

    private String name;

    private String color;

    private Long upStationId;

    private Long downStationId;

    private int distance;

    public Line toEntity(Station upStation, Station downStation) {
        return new Line(name, color, upStation, downStation, distance);
    }

}
