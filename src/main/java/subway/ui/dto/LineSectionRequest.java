package subway.ui.dto;

public class LineSectionRequest {
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    private LineSectionRequest() {}

    public LineSectionRequest(final Long upStationId, final Long downStationId, final Integer distance) {
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
