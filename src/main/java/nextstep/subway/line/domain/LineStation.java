package nextstep.subway.line.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table
public class LineStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long stationId;
    private Long preStationId;
    private Integer distance;
    private Integer duration;

    public LineStation() {
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

    public void updatePreStationTo(Long newPreStationId) {
        this.preStationId = newPreStationId;
    }
}