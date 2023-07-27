package subway.line.dto.request;

public class SectionRequest {

    private final Long id;
    private final Long downStationId;
    private final Long upStationId;
    private final Long distance;

    public SectionRequest(Long id, Long downStationId, Long upStationId, Long distance) {
        this.id = id;
        this.downStationId = downStationId;
        this.upStationId = upStationId;
        this.distance = distance;
    }

    public Long getId() {
        return id;
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
