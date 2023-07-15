package subway.dto;

public class SectionRequest {
    Long upStationId;
    Long downStationId;
    Long distance;

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }
}
