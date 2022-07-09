package nextstep.subway.domain;

import lombok.EqualsAndHashCode;
import nextstep.subway.domain.exception.OutOfBoundDistanceException;

import javax.persistence.Embeddable;

@Embeddable
@EqualsAndHashCode
public class Distance {

    public static final int MIN_VALUE = 1;

    private int value;

    protected Distance() {

    }

    public Distance(int value) {
        if (value < MIN_VALUE) {
            throw new OutOfBoundDistanceException(value);
        }
        this.value = value;
    }

    public int value() {
        return value;
    }
}
