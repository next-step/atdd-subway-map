package subway.line;

public class LineRemoveRequest {
    private Long stationId;

    public LineRemoveRequest(Long stationId) {
        this.stationId = stationId;
    }

    public Long getStationId() {
        return stationId;
    }
}
