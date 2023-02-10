package subway.exception;

public class IllegalSectionException extends RuntimeException {
    public IllegalSectionException() {
        super("해당 상행선 or 하행선은 등록할 수 없습니다.");
    }

    public IllegalSectionException(String message) {
        super(message);
    }
}
