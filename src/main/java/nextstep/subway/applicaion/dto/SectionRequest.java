package nextstep.subway.applicaion.dto;

public class SectionRequest {
    private Long downStationId;
    private Long upStationId;
    private Long distance;

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "SectionRequest{" +
                "downStationId=" + downStationId +
                ", upStationId=" + upStationId +
                ", distance=" + distance +
                '}';
    }
}
