package subway.exception;

public class NotFoundLineSectionException extends RuntimeException{

    public NotFoundLineSectionException() {
        this("Not Found LineSection");
    }

    public NotFoundLineSectionException(String message) {
        super(message);
    }

    public NotFoundLineSectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundLineSectionException(Throwable cause) {
        super(cause);
    }
}
