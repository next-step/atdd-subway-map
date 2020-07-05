package nextstep.subway.line.dto;

public class LineStationCreateRequest {
    private Long preStationId;
    private Long stationId;
    private Integer distance;
    private Integer duration;

    public LineStationCreateRequest() {
    }

    public LineStationCreateRequest(Long preStationId, Long stationId, Integer distance, Integer duration) {
        this.preStationId = preStationId;
        this.stationId = stationId;
        this.distance = distance;
        this.duration = duration;
    }

    public Long getPreStationId() {
        return preStationId;
    }

    public Long getStationId() {
        return stationId;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getDuration() {
        return duration;
    }

}
