package subway.exception;

import subway.domain.Distance;

public class InvalidSectionDistanceException extends RuntimeException {

    private static final String MESSAGE = "지하철 구간의 길이는 최소 %d 이상이어야 합니다 (현재 길이: %d)";

    public InvalidSectionDistanceException(long value) {
        super(String.format(MESSAGE, Distance.MIN_VALUE, value));
    }
}
