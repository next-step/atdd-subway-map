package subway.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import subway.exception.InvalidSectionDistanceException;

@Embeddable
public class Distance {

    public static final int MIN_VALUE = 1;

    @Column(name = "distance")
    private long value;

    protected Distance() {
    }

    public Distance(long value) {
        if (value < MIN_VALUE) {
            throw new InvalidSectionDistanceException(value);
        }
        this.value = value;
    }
}
