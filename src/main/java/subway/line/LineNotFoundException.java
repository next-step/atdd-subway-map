package subway.line;

public class LineNotFoundException extends RuntimeException {
    private static final String MESSAGE = "지하철노선을 찾을 수 없습니다.";

    public LineNotFoundException() {
        super(MESSAGE);
    }
}
