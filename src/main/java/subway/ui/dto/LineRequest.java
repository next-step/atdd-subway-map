package subway.ui.dto;

import subway.domain.Line;

public class LineRequest {

    private String name;
    private String color;
    private long upStationId;
    private long downStationId;
    private int distance;

    public LineRequest() {
    }

    public LineRequest(final String name, final String color, final long upStationId, final long downStationId, final int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public LineRequest(final String name, final String color) {
        this(name, color, 0L, 0L, 0);
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public long getUpStationId() {
        return upStationId;
    }

    public long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public Line toEntity() {
        return new Line(this.name, this.color);
    }
}
