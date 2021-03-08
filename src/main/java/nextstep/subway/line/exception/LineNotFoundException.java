package nextstep.subway.line.exception;

public class LineNotFoundException extends RuntimeException {

    private static final String LINE_NOT_FOUND_EXCEPTION = "지하철 노선을 찾을 수 없습니다.";

    public LineNotFoundException() {
        this(LINE_NOT_FOUND_EXCEPTION);
    }

    public LineNotFoundException(String message) {
        super(message);
    }
}
