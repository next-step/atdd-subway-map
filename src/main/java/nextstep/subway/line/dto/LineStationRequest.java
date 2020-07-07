package nextstep.subway.line.dto;

import nextstep.subway.line.domain.LineStation;

public class LineStationRequest {

    private Long preStationId;
    private Long stationId;
    private Integer distance;
    private Integer duration;

    public LineStationRequest(Long preStationId, Long stationId, Integer distance, Integer duration) {
        this.preStationId = preStationId;
        this.stationId = stationId;
        this.distance = distance;
        this.duration = duration;
    }

    public LineStation toLineStation() {
        return new LineStation(stationId, preStationId, distance, duration);
    }
}
