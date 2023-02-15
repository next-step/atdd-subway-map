package subway.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {

    private static final int MIN = 0;

    @Column(name = "distance")
    private int value;

    protected Distance() {
    }

    public Distance(final int distance) {
        if (distance < MIN) {
            throw new IllegalArgumentException("길이는 " + MIN + " 미만이 될 수 없습니다.");
        }
        this.value = distance;
    }

    public int getValue() {
        return value;
    }

    public Distance plus(Distance distance) {
        this.value += distance.getValue();
        return this;
    }

    public void minus(Distance distance) {
        this.value -= distance.getValue();
    }

    @Override
    public boolean equals(final Object o) {

        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Distance distance = (Distance) o;
        return value == distance.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
