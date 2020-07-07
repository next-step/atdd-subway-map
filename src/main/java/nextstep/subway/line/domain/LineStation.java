package nextstep.subway.line.domain;

import nextstep.subway.config.BaseEntity;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class LineStation extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public boolean isSame(LineStation newLineStation) {
        return Objects.equals(this.stationId, newLineStation.stationId);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LineStation)) return false;
        LineStation that = (LineStation) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(getPreStationId(), that.getPreStationId()) &&
                Objects.equals(getStationId(), that.getStationId()) &&
                Objects.equals(getDistance(), that.getDistance()) &&
                Objects.equals(getDuration(), that.getDuration());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, getPreStationId(), getStationId(), getDistance(), getDuration());
    }
}
