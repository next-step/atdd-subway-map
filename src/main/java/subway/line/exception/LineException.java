package subway.line.exception;

public class LineException extends IllegalArgumentException {

    public LineException() {
    }

    public LineException(String s) {
        super(s);
    }

    public LineException(String message, Throwable cause) {
        super(message, cause);
    }

    public LineException(Throwable cause) {
        super(cause);
    }
}
