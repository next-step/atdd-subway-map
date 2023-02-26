package subway.line.presentation;

public class SectionRequest {
    private Long upStationId;

    private Long downStationId;

    private long distance;

    public SectionRequest() {
    }

    public SectionRequest(Long upStationId, Long downStationId, long distance) {
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

    public long getDistance() {
        return distance;
    }
}
