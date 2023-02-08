package subway.exception;

public class ReduceSectionException extends RuntimeException {
    public ReduceSectionException() {
        super();
    }

    public ReduceSectionException(String message) {
        super(message);
    }

    public ReduceSectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReduceSectionException(Throwable cause) {
        super(cause);
    }

    protected ReduceSectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
