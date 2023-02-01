package subway.exception;

public class CannotDeleteStationFromALineWithOnlyTwoStationsException extends RuntimeException {
    public CannotDeleteStationFromALineWithOnlyTwoStationsException() {
        super();
    }

    public CannotDeleteStationFromALineWithOnlyTwoStationsException(String message) {
        super(message);
    }

    public CannotDeleteStationFromALineWithOnlyTwoStationsException(String message, Throwable cause) {
        super(message, cause);
    }

    public CannotDeleteStationFromALineWithOnlyTwoStationsException(Throwable cause) {
        super(cause);
    }

    protected CannotDeleteStationFromALineWithOnlyTwoStationsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
