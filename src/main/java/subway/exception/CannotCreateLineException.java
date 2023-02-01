package subway.exception;

public class CannotCreateLineException extends RuntimeException {

    private static final String MESSAGE = "상행역과 하행역이 동일합니다.";

    public CannotCreateLineException() {
        super(MESSAGE);
    }
}
