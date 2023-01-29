package subway.ui.dto;

import subway.domain.Line;
import subway.domain.Station;

public class LineRequest {

    private String name;
    private String color;
    private long upStationId;
    private long downStationId;
    private int distance;

    private LineRequest() {}

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

    public Line toEntity(final Station upStation, final Station downStation) {
        return new Line(this.name, this.color, upStation, downStation, this.distance);
    }
}
