package subway.model;

public class CreateSectionRequest {
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

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
