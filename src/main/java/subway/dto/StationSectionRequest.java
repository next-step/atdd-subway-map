package subway.dto;

public class StationSectionRequest {

    private Long upStationId;

    private Long downStationId;

    private int distance;

    private Long stationLineId;

    public StationSectionRequest() {
    }

    public StationSectionRequest(Long upStationId, Long downStationId, int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public StationSectionRequest(Long upStationId, Long downStationId, int distance, Long stationLineId) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
        this.stationLineId = stationLineId;
    }

    public static StationSectionRequest mergeForCreateLine(Long stationLineId, StationSectionRequest request) {
        return new StationSectionRequest(
                request.getUpStationId(),
                request.getDownStationId(),
                request.getDistance(),
                stationLineId
        );
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
