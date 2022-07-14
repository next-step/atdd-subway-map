package nextstep.subway.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {

    @Column(name = "distance")
    private int value;

    protected Distance() {
    }

    public Distance(final int value) {
        valueValidation(value);
        this.value = value;
    }

    private void valueValidation(final int value) {
        if (value <= 0) {
            throw new IllegalArgumentException("지하철 노선의 거리 값이 0보다 커야합니다.");
        }
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
