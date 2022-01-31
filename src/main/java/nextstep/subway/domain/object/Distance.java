package nextstep.subway.domain.object;

import java.security.InvalidParameterException;

public class Distance {
    private int value;

    public Distance(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public void checkDistanceLessThanZero() {
        if (this.value < 0) {
            throw new InvalidParameterException();
        }
    }
}
