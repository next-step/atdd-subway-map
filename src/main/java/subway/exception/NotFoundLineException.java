package subway.exception;

public class NotFoundLineException extends RuntimeException {

    public NotFoundLineException() {
        this("Not Found Line");
    }

    public NotFoundLineException(String message) {
        super(message);
    }

    public NotFoundLineException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundLineException(Throwable cause) {
        super(cause);
    }
}
