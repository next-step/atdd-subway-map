package nextstep.subway.exception;

public class DuplicatedLineException extends RuntimeException{
    public DuplicatedLineException() {
        super();
    }

    public DuplicatedLineException(String message) {
        super(message);
    }

    public DuplicatedLineException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicatedLineException(Throwable cause) {
        super(cause);
    }

    protected DuplicatedLineException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
