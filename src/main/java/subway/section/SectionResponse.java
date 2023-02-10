package subway.section;

public class SectionResponse {
    private Long downStationId;
    private Long upStationId;
    private Long distance;

    public SectionResponse() {
    }

    public SectionResponse(Long downStationId, Long upStationId, Long distance) {
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.distance = distance;
    }

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
