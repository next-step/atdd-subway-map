package subway.exception;

public class NotFoundSectionException extends RuntimeException {

    public NotFoundSectionException() {
        this("Not Found Section");
    }

    public NotFoundSectionException(String message) {
        super(message);
    }

    public NotFoundSectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundSectionException(Throwable cause) {
        super(cause);
    }
}
