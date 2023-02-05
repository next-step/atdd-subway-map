package subway.domain;

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
}
