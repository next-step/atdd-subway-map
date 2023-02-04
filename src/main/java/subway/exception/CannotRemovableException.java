package subway.exception;

public class CannotRemovableException extends SectionException {
    public CannotRemovableException() {
        super();
    }

    public CannotRemovableException(String message) {
        super(message);
    }

    public CannotRemovableException(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotRemovableException(Throwable cause) {
        super(cause);
    }

    public CannotRemovableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
