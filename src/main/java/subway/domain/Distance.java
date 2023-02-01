package subway.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {

    private static final int DISTANCE_MIN = 0;

    @Column(name = "distance")
    private int value;

    protected Distance() {
    }

    public Distance(final int distance) {
        if (distance < DISTANCE_MIN) {
            throw new IllegalArgumentException("길이는 " + DISTANCE_MIN + " 미만이 될 수 없습니다.");
        }
        this.value = distance;
    }

    public int getValue() {
        return value;
    }
}
