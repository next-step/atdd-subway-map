package nextstep.subway.exception.line;

public class LineBlankNameException extends LineBusinessException {
    private static final String MESSAGE = "blank line name occurred";

    public LineBlankNameException() {
        super(MESSAGE);
    }
}
