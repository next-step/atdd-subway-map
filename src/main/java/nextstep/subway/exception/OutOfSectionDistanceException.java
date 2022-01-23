package nextstep.subway.exception;

public class OutOfSectionDistanceException extends IllegalArgumentException {
    private static final String MESSAGE = "거리는 1이상 이어야 합니다. 입력값::";
    public OutOfSectionDistanceException(int distance) {
        super(MESSAGE + distance);
    }
}
