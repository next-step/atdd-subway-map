package nextstep.subway.line.exception;

public class RemoveNonExistStationInLineException extends RuntimeException {
    public RemoveNonExistStationInLineException() {
    }

    public RemoveNonExistStationInLineException(String message) {
        super(message);
    }

    public RemoveNonExistStationInLineException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoveNonExistStationInLineException(Throwable cause) {
        super(cause);
    }

    public RemoveNonExistStationInLineException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
