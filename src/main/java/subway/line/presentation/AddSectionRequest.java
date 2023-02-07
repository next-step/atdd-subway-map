package subway.line.presentation;

public class AddSectionRequest {
    private Long downStationId;
    private Long upStationId;
    private Long distance;

    public AddSectionRequest() {
    }

    public AddSectionRequest(Long downStationId, Long upStationId, Long distance) {
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
