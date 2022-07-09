package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import nextstep.subway.domain.exception.OutOfBoundDistanceException;

import javax.persistence.Embeddable;

@Embeddable
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Distance {

    public static final int MIN_VALUE = 1;

    private int value;

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
