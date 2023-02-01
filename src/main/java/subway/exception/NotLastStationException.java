package subway.exception;

public class NotLastStationException extends RuntimeException {

    public NotLastStationException() {
        this("Not Last Station");
    }

    public NotLastStationException(String message) {
        super(message);
    }

    public NotLastStationException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotLastStationException(Throwable cause) {
        super(cause);
    }
}
