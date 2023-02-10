package subway.dto;

public class LineSectionRequest {

    private Long upStationId;

    private Long downStationId;

    private Integer distance;

    public LineSectionRequest() {
    }

    public LineSectionRequest(Long upStationId, Long downStationId, Integer distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Integer getDistance() {
        return distance;
    }
}
