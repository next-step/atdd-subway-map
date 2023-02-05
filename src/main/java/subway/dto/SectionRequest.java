package subway.dto;

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
}
