package nextstep.subway.exception;

public class NotExistsStationIdException extends RuntimeException{
    public NotExistsStationIdException() {
        super();
    }

    public NotExistsStationIdException(String message) {
        super(message);
    }

    public NotExistsStationIdException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotExistsStationIdException(Throwable cause) {
        super(cause);
    }

    protected NotExistsStationIdException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
