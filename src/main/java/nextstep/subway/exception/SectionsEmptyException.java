package nextstep.subway.exception;

public class SectionsEmptyException extends RuntimeException {
    private static final String MESSAGE = "stations is empty";

    public SectionsEmptyException() {
        super(MESSAGE);
    }
}
