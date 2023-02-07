package subway.dto;

import subway.domain.Line;
import subway.domain.Station;

public class LineRequest {

    private String name;
    private String color;
    private Long downStationId;
    private Long upStationId;
    private Long distance;

    private LineRequest(String name, String color, Long downStationId, Long upStationId, Long distance) {
        this.name = name;
        this.color = color;
        this.downStationId = downStationId;
        this.upStationId = upStationId;
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
            String name, String color, Long downStationId, Long upStationId, Long distance) {
        return new LineRequest(name, color, downStationId, upStationId, distance);
    }

    public Line toEntity(Station downStation, Station upStation) {
        return new Line(
                this.name,
                this.color,
                downStation,
                upStation,
                this.distance
        );
    }
}
