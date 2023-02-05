package subway.dto;

import subway.domain.Line;
import subway.domain.Station;

public class LineRequest {

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Long distance;

    private LineRequest(String name, String color, Long upStationId, Long downStationId, Long distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
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

    public Long getDistance() {
        return distance;
    }

    public static LineRequest of(
            String name, String color, Long upStationId, Long downStationId, Long distance) {
        return new LineRequest(name, color, upStationId, downStationId, distance);
    }

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
