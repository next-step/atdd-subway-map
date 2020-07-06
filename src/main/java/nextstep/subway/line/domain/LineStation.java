package nextstep.subway.line.domain;

import nextstep.subway.config.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class LineStation extends BaseEntity {
    public static final int ZERO = 0;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long preStationId;
    private Long stationId;
    private Integer duration;
    private Integer distance;

    protected LineStation() {

    }

    public LineStation(Long preStationId, Long stationId, Integer duration, Integer distance) {
        this.preStationId = preStationId;
        this.stationId = stationId;
        this.duration = duration;
        this.distance = distance;
    }


    public Long getId() {
        return id;
    }

    public Long getPreStationId() {
        return preStationId;
    }

    public Long getStationId() {
        return stationId;
    }

    public Integer getDuration() {
        return duration;
    }

    public Integer getDistance() {
        return distance;
    }

    public void updatePreStationId(Long preStationId) {
        this.preStationId = preStationId;
    }

    public boolean isSamePreStationId(LineStation lineStation) {
        if (preStationId == null) {
            return lineStation == null;
        }
        return this.preStationId.equals(lineStation.getPreStationId());
    }

    public boolean isStartStation() {
        return this.preStationId == null;
    }

    public boolean isPreStation(LineStation station) {
        return station.getStationId().equals(this.preStationId);
    }
}
