package subway.exception;

public class CannotRemoveLineSectionException extends RuntimeException {

    public CannotRemoveLineSectionException() {
        this("Cannot Remove LineSection");
    }

    public CannotRemoveLineSectionException(String message) {
        super(message);
    }

    public CannotRemoveLineSectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotRemoveLineSectionException(Throwable cause) {
        super(cause);
    }
}
