package subway.line.presentation.request;

public class AddSectionRequest {

    private Long downStationId;

    private Long upStationId;

    private Integer distance;

    private AddSectionRequest() {
    }

    private AddSectionRequest(Long downStationId, Long upStationId, Integer distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static AddSectionRequest of(Long downStationId, Long upStationId, Integer distance) {
        return new AddSectionRequest(downStationId, upStationId, distance);
    }

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
