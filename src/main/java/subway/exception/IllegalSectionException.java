package subway.exception;

public class IllegalSectionException extends RuntimeException {
    private static final String message = "새로운 구간의 상한선이 노선의 하행종점역이 아닙니다.";

    public IllegalSectionException() {
        super(message);
    }
}
