package nextstep.subway.exception.line;

public class LineBlankColorException extends LineBusinessException {
    private static final String MESSAGE = "blank line color occurred";

    public LineBlankColorException() {
        super(MESSAGE);
    }
}
