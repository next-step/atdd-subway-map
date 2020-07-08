package nextstep.subway.line.exception;

public class DuplicateStationInLineException extends RuntimeException {
    public DuplicateStationInLineException() {
    }

    public DuplicateStationInLineException(String message) {
        super(message);
    }

    public DuplicateStationInLineException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateStationInLineException(Throwable cause) {
        super(cause);
    }

    public DuplicateStationInLineException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
