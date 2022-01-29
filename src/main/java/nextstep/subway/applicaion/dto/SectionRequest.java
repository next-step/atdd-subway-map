package nextstep.subway.applicaion.dto;

import javax.validation.constraints.NotNull;

public class SectionRequest {
    @NotNull
    private Long upStationId;
    @NotNull
    private Long downStationId;
    private int distance;

    public long getUpStationId() {
        return upStationId;
    }

    public long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }
}
