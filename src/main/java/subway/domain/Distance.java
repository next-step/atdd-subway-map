package subway.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {

    private static final int MIN_VALUE = 1;

    @Column(name = "distance")
    private long value;

    protected Distance() {
    }

    public Distance(long value) {
        if (value < MIN_VALUE) {
            throw new IllegalArgumentException("지하철 구간의 길이는 1 이상이어야 합니다.");
        }
        this.value = value;
    }
}
