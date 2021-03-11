package nextstep.subway.line.exception;

public class LineNotFoundException extends RuntimeException {

    private static final String EXCEPTION_DESCRIPTION = "지하철 노선을 찾을 수 없습니다.";

    public LineNotFoundException() {
        this(EXCEPTION_DESCRIPTION);
    }

    public LineNotFoundException(String message) {
        super(message);
    }
}
