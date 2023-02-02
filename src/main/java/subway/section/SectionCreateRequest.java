package subway.section;

public class SectionCreateRequest {

    private final Long downStationId;

    private final Long upStationId;

    private final Long distance;

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDistance() {
        return distance;
    }

    public SectionCreateRequest(Long downStationId, Long upStationId, Long distance) {
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.distance = distance;
    }
}
