package subway.exception;

public class LineNotFoundException extends SubwayException {
    public static final String EXCEPTION_MESSAGE = "지하철노선을 찾을 수 없습니다.";

    public LineNotFoundException() {
        super(EXCEPTION_MESSAGE);
    }
}
