package nextstep.subway.applicaion.dto;

public class SectionRequest {
    private Long downStationId;
    private Long upStationId;
    private Integer distance;

    public SectionRequest() {
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Integer getDistance() {
        return distance;
    }
}
