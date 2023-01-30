package subway.line;

import subway.station.Station;

public class LineCreateRequest {
    private final String name;
    private final String color;
    private final Long upStationId;
    private final Long downStationId;
    private final Integer distance;

    private LineCreateRequest(final String name, final String color, final Long upStationId, final Long downStationId, final Integer distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Line toEntity(Station upStation, Station downStation) {
        return new Line.Builder()
                .name(this.name)
                .color(this.color)
                .upStation(upStation)
                .downStation(downStation)
                .distance(this.distance)
                .build();
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Integer getDistance() {
        return distance;
    }
}
