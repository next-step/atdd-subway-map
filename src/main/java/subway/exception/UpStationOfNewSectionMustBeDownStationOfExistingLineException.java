package subway.exception;

public class UpStationOfNewSectionMustBeDownStationOfExistingLineException extends RuntimeException {
    public UpStationOfNewSectionMustBeDownStationOfExistingLineException() {
        super();
    }

    public UpStationOfNewSectionMustBeDownStationOfExistingLineException(String message) {
        super(message);
    }

    public UpStationOfNewSectionMustBeDownStationOfExistingLineException(String message, Throwable cause) {
        super(message, cause);
    }

    public UpStationOfNewSectionMustBeDownStationOfExistingLineException(Throwable cause) {
        super(cause);
    }

    protected UpStationOfNewSectionMustBeDownStationOfExistingLineException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
