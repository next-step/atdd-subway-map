package nextstep.subway.line.exception;

public class LineNameDuplicatedException extends RuntimeException {

    private static final String LINE_NAME_DUPLICATED_EXCEPTION = "지하철 노선 이름은 중복될 수 없습니다.";

    public LineNameDuplicatedException() {
        this(LINE_NAME_DUPLICATED_EXCEPTION);
    }

    public LineNameDuplicatedException(String message) {
        super(message);
    }
}
