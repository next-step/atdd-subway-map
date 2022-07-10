package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;
import nextstep.subway.domain.Station;

import java.util.List;

public class LineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private int distance;

    public Line toEntity(List<Station> station) {
        return new Line(null, this.getName(), this.getColor(), this.getDistance(), station);
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
