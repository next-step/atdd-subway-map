package nextstep.subway.line.exception;

public class LineNameDuplicatedException extends RuntimeException {

    private static final String EXCEPTION_DESCRIPTION = "지하철 노선 이름은 중복될 수 없습니다.";

    public LineNameDuplicatedException() {
        this(EXCEPTION_DESCRIPTION);
    }

    public LineNameDuplicatedException(String message) {
        super(message);
    }
}
