package nextstep.subway.line.domain;

import nextstep.subway.config.BaseEntity;

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
    private Long prevStationId;
    private Long stationId;
    private Integer duration;
    private Integer distance;

    protected LineStation() {

    }

    public LineStation(Long prevStationId, Long stationId, Integer duration, Integer distance) {
        validateParams(prevStationId, stationId, duration, distance);
        this.prevStationId = prevStationId;
        this.stationId = stationId;
        this.duration = duration;
        this.distance = distance;
    }

    private void validateParams(Long prevStationId, Long stationId, Integer duration, Integer distance) {
        Objects.requireNonNull(stationId);
        Objects.requireNonNull(duration);
        Objects.requireNonNull(distance);

        if (stationId <= ZERO || duration <= ZERO || distance <= ZERO) {
            throw new IllegalArgumentException("must be greater than " + ZERO);
        }
    }
}
