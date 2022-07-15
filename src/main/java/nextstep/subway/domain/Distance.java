package nextstep.subway.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance {
    private static final long MIN_DISTANCE = 1;
    @Column
    private long distance;

    protected Distance() {

    }

    public Distance(long distance) {
        if (distance < MIN_DISTANCE) {
            throw new IllegalArgumentException(String.format("거리는 %d 이상이어야 합니다.", MIN_DISTANCE));
        }
        this.distance = distance;
    }

    public long getDistance() {
        return distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance distance1 = (Distance) o;
        return distance == distance1.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(distance);
    }
}
