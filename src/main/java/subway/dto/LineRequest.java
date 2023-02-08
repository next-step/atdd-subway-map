package subway.dto;

import subway.domain.Line;

public class LineRequest implements EntityTransformable<Line> {

    private String name;

    private String color;

    private long upStationId;

    private long downStationId;

    private int distance;

    private LineRequest() {
    }

    public LineRequest(String name, String color) {
        this(name, color, 0L, 0L, 0);
    }

    public LineRequest(String name, String color, long upStationId, long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    @Override
    public Line toEntity() {
        return new Line(
                this.getName(),
                this.getColor()
        );
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

    public int getDistance() {
        return distance;
    }
}