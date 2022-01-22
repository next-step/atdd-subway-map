package nextstep.subway.exception;

public class DuplicatedStationNameException extends RuntimeException{
    public DuplicatedStationNameException() {
        super();
    }

    public DuplicatedStationNameException(String message) {
        super(message);
    }

    public DuplicatedStationNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicatedStationNameException(Throwable cause) {
        super(cause);
    }

    protected DuplicatedStationNameException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
