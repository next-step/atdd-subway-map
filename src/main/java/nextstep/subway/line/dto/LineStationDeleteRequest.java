package nextstep.subway.line.dto;

public class LineStationDeleteRequest {
    private Long stationId;

    public LineStationDeleteRequest(Long stationId) {
        this.stationId = stationId;
    }

    public Long getStationId() {
        return stationId;
    }
}
