package subway.line;

import lombok.Getter;
import subway.station.Station;

@Getter
public class LineCreateRequest {
    private final String name;
    private final String color;
    private final Long upStationId;
    private final Long downStationId;
    private final Integer distance;

    public LineCreateRequest(final String name, final String color, final Long upStationId, final Long downStationId, final Integer distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Line toEntity(Station upStation, Station downStation) {
        return new Line(this.name, this.color, new Section(downStation, upStation, distance));
    }

}
