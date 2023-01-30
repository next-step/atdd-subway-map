package subway.exception;

public class LineNotFoundException extends RuntimeException {
    public LineNotFoundException() {
        super("지하철노선을 찾을 수 없습니다.");
    }
}
