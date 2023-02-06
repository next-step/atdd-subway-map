package subway.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {

    public static final int LOWER_LIMIT = 0;
    @Column(name = "distance")
    private int value;

    protected Distance() {}

    public Distance(int value) {
        if (value <= LOWER_LIMIT) {
            throw new IllegalArgumentException(
                    String.format("지하철 구간 길이의 하한값은 %d 입니다. (하한값: %d)", LOWER_LIMIT)
            );
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
