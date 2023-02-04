package subway.exception;

public abstract class CannotAppendableException extends SectionException {
    public CannotAppendableException() {
        super();
    }

    public CannotAppendableException(String message) {
        super(message);
    }

    public CannotAppendableException(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotAppendableException(Throwable cause) {
        super(cause);
    }

    public CannotAppendableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
