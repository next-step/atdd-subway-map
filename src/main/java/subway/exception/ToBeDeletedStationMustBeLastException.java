package subway.exception;

public class ToBeDeletedStationMustBeLastException extends RuntimeException {
    public ToBeDeletedStationMustBeLastException() {
        super();
    }

    public ToBeDeletedStationMustBeLastException(String message) {
        super(message);
    }

    public ToBeDeletedStationMustBeLastException(String message, Throwable cause) {
        super(message, cause);
    }

    public ToBeDeletedStationMustBeLastException(Throwable cause) {
        super(cause);
    }

    protected ToBeDeletedStationMustBeLastException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
