package nextstep.subway.applicaion.dto;

public class SectionRequest {
    private Long downStationId;
    private Long upStationId;
    private Integer distance;

    public Integer getDistance() {
        return distance;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }
}
