package nextstep.subway.line.exception;

public class NonExistStationInLineException extends RuntimeException {
    public NonExistStationInLineException() {
    }

    public NonExistStationInLineException(String message) {
        super(message);
    }

    public NonExistStationInLineException(String message, Throwable cause) {
        super(message, cause);
    }

    public NonExistStationInLineException(Throwable cause) {
        super(cause);
    }

    public NonExistStationInLineException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
