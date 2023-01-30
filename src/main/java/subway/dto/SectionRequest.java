package subway.dto;

public class SectionRequest {

    private Long upStationId;
    private Long downStationId;
    private long distance;

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
