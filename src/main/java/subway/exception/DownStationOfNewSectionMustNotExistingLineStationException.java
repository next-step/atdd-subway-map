package subway.exception;

public class DownStationOfNewSectionMustNotExistingLineStationException extends RuntimeException {
    public DownStationOfNewSectionMustNotExistingLineStationException() {
        super();
    }

    public DownStationOfNewSectionMustNotExistingLineStationException(String message) {
        super(message);
    }

    public DownStationOfNewSectionMustNotExistingLineStationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DownStationOfNewSectionMustNotExistingLineStationException(Throwable cause) {
        super(cause);
    }

    protected DownStationOfNewSectionMustNotExistingLineStationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
