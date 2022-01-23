package nextstep.subway.exception;

public class InvalidSectionException extends IllegalArgumentException {
    private static final String MESSAGE = "상행종점 혹은 하행종점이 아닙니다.";

    public InvalidSectionException() {
        super(MESSAGE);
    }
}
