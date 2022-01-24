package nextstep.subway.exception.line;

public class LineDuplicateNameException extends LineBusinessException {
    private static final String MESSAGE = "duplicate line name occurred";

    public LineDuplicateNameException() {
        super(MESSAGE);
    }
}
