package subway.line.application.dto.request;

import lombok.Getter;
import subway.line.domain.Line;
import subway.station.domain.Station;

@Getter
public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Long distance;

    public LineRequest(final String name, final String color,
                       final Long upStationId, final Long downStationId, final Long distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Line toEntity(final Station upStation, final Station downStation) {
        return Line.builder()
                .name(getName())
                .color(getColor())
                .upStation(upStation)
                .downStation(downStation)
                .distance(getDistance())
                .build();
    }
}
