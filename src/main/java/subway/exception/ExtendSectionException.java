package subway.exception;

public class ExtendSectionException extends RuntimeException {
    public ExtendSectionException() {
        super();
    }

    public ExtendSectionException(String message) {
        super(message);
    }

    public ExtendSectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExtendSectionException(Throwable cause) {
        super(cause);
    }

    protected ExtendSectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
