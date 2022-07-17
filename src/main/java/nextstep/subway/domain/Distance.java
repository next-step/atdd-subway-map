package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import nextstep.subway.exception.InvalidDistanceValueException;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Distance {

    private static final int ZERO = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int upStationDistance;

    private int downStationDistance;

    public Distance(int upStationDistance, int downStationDistance) {
        validateDistance(upStationDistance);
        validateDistance(downStationDistance);
        this.upStationDistance = upStationDistance;
        this.downStationDistance = downStationDistance;
    }

    private void validateDistance(int distance) {
        if (distance < ZERO) {
            throw new InvalidDistanceValueException(distance);
        }
    }

    public int getUpStationDistance() {
        return upStationDistance;
    }

    public int getDownStationDistance() {
        return downStationDistance;
    }
    
}
