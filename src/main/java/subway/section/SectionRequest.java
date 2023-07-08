package subway.section;

public class SectionRequest {
    private Long downStationId;
    private Long distance;

    public SectionRequest(Long downStationId, Long distance) {
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }
}
