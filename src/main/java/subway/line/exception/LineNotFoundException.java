package subway.line.exception;

public class LineNotFoundException extends RuntimeException {
    private static final String MESSAGE = "지하철 노선이 존재하지 않습니다.";

    public LineNotFoundException() {
        super(MESSAGE);
    }

}