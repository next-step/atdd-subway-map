package nextstep.subway.exception;

public class DuplicatedStationException extends RuntimeException{
    public DuplicatedStationException() {
        super();
    }

    public DuplicatedStationException(String message) {
        super(message);
    }

    public DuplicatedStationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicatedStationException(Throwable cause) {
        super(cause);
    }

    protected DuplicatedStationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
