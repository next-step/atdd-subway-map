package nextstep.subway.exception;

public class OutOfSectionSizeException extends IllegalArgumentException {
    private static final String MESSAGE = "구간의 길이는 최소 1 이상이어야 합니다.";

    public OutOfSectionSizeException() {
        super(MESSAGE);
    }
}
