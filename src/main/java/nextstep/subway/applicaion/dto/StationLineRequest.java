package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.Line;

public class StationLineRequest {
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;

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

    public Line toEntity() {
        return new Line(name, color, upStationId, downStationId);
    }
}
