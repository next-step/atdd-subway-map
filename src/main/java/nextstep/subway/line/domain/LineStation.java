package nextstep.subway.line.domain;

import nextstep.subway.config.BaseEntity;
import nextstep.subway.exception.FieldValidationException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

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
        validateParameters(preStationId, stationId, duration, distance);
        this.preStationId = preStationId;
        this.stationId = stationId;
        this.duration = duration;
        this.distance = distance;
    }

    private void validateParameters(Long preStationId, Long stationId, Integer duration, Integer distance) {
        Objects.requireNonNull(duration);
        Objects.requireNonNull(distance);

        if (preStationId != null && preStationId <= 0) {
            throw new FieldValidationException("preStationId는 0보다 커야 합니다.");
        }

        if (stationId != null && stationId <= 0) {
            throw new FieldValidationException("stationId는 0보다 커야 합니다.");
        }

        if (duration <= 0) {
            throw new FieldValidationException("duration은 0보다 커야 합니다.");
        }

        if (distance <= 0) {
            throw new FieldValidationException("distance는 0보다 커야 합니다.");
        }
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
            return lineStation.getPreStationId() == null;
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
