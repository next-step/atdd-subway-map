package subway.dto;

public class StationSectionRequest {

    private Long upStationId;

    private Long downStationId;

    private int distance;

    private Long stationLineId;

    public StationSectionRequest(Long upStationId, Long downStationId, int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public StationSectionRequest updateStationLineId(Long stationLineId) {
        this.stationLineId = stationLineId;
        return this;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }

    public Long getStationLineId() {
        return stationLineId;
    }
}
