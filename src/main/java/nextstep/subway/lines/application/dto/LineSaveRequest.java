package nextstep.subway.lines.application.dto;

import nextstep.subway.lines.domain.Line;

public class LineSaveRequest {

    private String name;

    private String color;

    private Long upStationId;

    private Long downStationId;

    private int distance;

    public String getName() {
        return this.name;
    }

    public String getColor() {
        return this.color;
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
