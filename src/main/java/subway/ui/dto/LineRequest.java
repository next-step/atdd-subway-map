package subway.ui.dto;

import subway.domain.Line;
import subway.domain.Station;

public class LineRequest {

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    private LineRequest() {}

    public LineRequest(final String name, final String color, final Long upStationId, final Long downStationId, final Integer distance) {
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

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Line toEntity(final Station upStation, final Station downStation) {
        return new Line(this.name, this.color, upStation, downStation, this.distance);
    }
}
