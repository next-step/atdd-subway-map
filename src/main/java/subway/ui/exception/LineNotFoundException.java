package subway.ui.exception;

public class LineNotFoundException extends NotFoundException {

    private static final String ERROR_MESSAGE = "존재하지 않는 Line 입니다.";

    public LineNotFoundException() {
        super(ERROR_MESSAGE);
    }
}
