package subway.line;

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
            throw new InvalidDistanceException(value);
        }
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
