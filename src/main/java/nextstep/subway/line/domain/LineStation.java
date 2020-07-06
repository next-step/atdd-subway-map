package nextstep.subway.line.domain;

import javax.persistence.*;

@Embeddable
public class LineStation{
    private Long preStationId;
    private Long stationId;
    private Integer distance;
    private Integer duration;

    protected LineStation() {
    }

    public LineStation(Long stationId, Long preStationId, Integer distance, Integer duration) {
        this.stationId = stationId;
        this.preStationId = preStationId;
        this.distance = distance;
        this.duration = duration;
    }

    public void updatePreStation(Long stationId) {
        this.preStationId = stationId;
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
