package nextstep.subway.applicaion.dto;

import nextstep.subway.domain.object.Distance;

import javax.validation.constraints.NotNull;

public class LineRequest {
    private String name;
    private String color;
    @NotNull
    private Long upStationId;
    @NotNull
    private Long downStationId;
    @NotNull
    private Distance distance;

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

    public Distance getDistance() {
        return distance;
    }
}
