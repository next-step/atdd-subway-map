package nextstep.subway.domain.exception;

import nextstep.subway.domain.Distance;

public class OutOfBoundDistanceException extends IllegalArgumentException {

    private static final String MESSAGE = "%d 보다 작을 수 없습니다. 입력한 값=%d";

    public OutOfBoundDistanceException(int distance) {
        super(String.format(MESSAGE, Distance.MIN_VALUE, distance));
    }
}
