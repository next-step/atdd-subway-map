package nextstep.subway.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance {

    private long distance;

    protected Distance() {

    }

    public Distance(long distance) {
        if (distance < 1) {
            throw new IllegalArgumentException("거리는 1 이상이어야 합니다.");
        }
        this.distance = distance;
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
