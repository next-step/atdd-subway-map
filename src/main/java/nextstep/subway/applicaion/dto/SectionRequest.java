package nextstep.subway.applicaion.dto;

public class SectionRequest {
    private Long upStationId;
    private Long downStationId;
    private int distance;

    public int getDistance() {
        return distance;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getUpStationId() {
        return upStationId;
    }
}
