package subway.stationline.dto;

public class StationLineExtendRequest {
    private Long upStationId;
    private Long downStationId;
    private Long distance;

    public StationLineExtendRequest() {
    }

    public StationLineExtendRequest(Long upStationId, Long downStationId, Long distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }
}
