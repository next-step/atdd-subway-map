package nextstep.subway.line.domain;

import nextstep.subway.config.BaseEntity;

import javax.persistence.*;

@Entity
public class LineStation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long stationId;
    private Long preStationId;
    private Integer distance;
    private Integer duration;

    public LineStation() {}

    public LineStation(Long stationId, Long preStationId, Integer distance, Integer duration) {
        this.stationId = stationId;
        this.preStationId = preStationId;
        this.distance = distance;
        this.duration = duration;
    }

    public Long getStationId() {
        return stationId;
    }

    public Long getPreStationId() {
        return preStationId;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getDuration() {
        return duration;
    }

    public boolean isSamePreStation(Long preStationId) {
        if (this.preStationId == null || preStationId == null) {
            return false;
        }

        return this.preStationId.equals(preStationId);
    }

    public void relocateAfter(Long preStationId) {
        this.preStationId = preStationId;
    }
}
