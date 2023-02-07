package subway.dto;

public class SectionRequest {
    private Long downStationId;
    private Long upStationId;
    private Long distance;

    private SectionRequest(Long downStationId, Long upStationId, Long distance) {
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.distance = distance;
    }

    public static SectionRequest of(Long downStationId, Long upStationId, Long distance) {
        return new SectionRequest(downStationId, upStationId, distance);
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
