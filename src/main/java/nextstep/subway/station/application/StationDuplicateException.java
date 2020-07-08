package nextstep.subway.station.application;

public class StationDuplicateException extends RuntimeException {
    public StationDuplicateException() {
        super();
    }

    public StationDuplicateException(String message) {
        super(message);
    }

    public StationDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public StationDuplicateException(Throwable cause) {
        super(cause);
    }

    protected StationDuplicateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
