package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Line;
import nextstep.subway.line.domain.LineStation;

public class LineStationRequest {
    private Long preStationId;
    private Long stationId;
    private Integer distance;
    private Integer duration;

    public LineStationRequest() {
    }

    public LineStationRequest(Long preStationId, Long stationId, Integer distance, Integer duration) {
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

    public Line toLineStation() {
        return new LineStation(preStationId, stationId, distance, duration);
    }
}
