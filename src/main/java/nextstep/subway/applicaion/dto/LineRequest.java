package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;

public class LineRequest {

    private String name;

    private String color;

    private Long upStationId;

    private Long downStationId;

    private Long distance;

    public LineRequest(String name, String color, Long upStationId, Long downStationId, Long distance) {
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

    public Line toLine(Line line) {
        return new Line(
                line.getId(),
                this.name != null ? this.name : line.getName(),
                this.color != null ? this.color : line.getColor(),
                this.upStationId != null ? this.upStationId : line.getUpStationId(),
                this.downStationId != null ? this.downStationId : line.getDownStationId(),
                this.distance != null ? this.distance : line.getDistance()
        );
    }
}
