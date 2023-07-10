package subway.section;

public class SectionRequest {
    private Long downStationId;
    private Long upStationId;
    private Integer distance;

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
