package nextstep.subway.exception.line;

public class LineDuplicateColorException extends LineBusinessException {
    private static final String MESSAGE = "duplicate line color occurred";

    public LineDuplicateColorException() {
        super(MESSAGE);
    }
}
