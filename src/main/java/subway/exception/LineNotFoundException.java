package subway.exception;

public class LineNotFoundException extends RuntimeException {

    public LineNotFoundException() {
    }

    public LineNotFoundException(String message) {
        super(message);
    }

    public LineNotFoundException(Throwable cause) {
        super(cause);
    }
}
