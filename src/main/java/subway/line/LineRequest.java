package subway.line;

import subway.section.Section;

public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;

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

    public Line createLine() {
        return new Line(name, color, upStationId, downStationId, distance);
    }

    public Section createSection() {
        return new Section(upStationId, downStationId, distance);
    }
}
