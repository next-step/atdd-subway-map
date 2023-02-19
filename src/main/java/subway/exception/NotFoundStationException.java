package subway.exception;

public class NotFoundStationException extends RuntimeException {

    public NotFoundStationException() {
        this("Not Found Station");
    }

    public NotFoundStationException(String message) {
        super(message);
    }

    public NotFoundStationException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotFoundStationException(Throwable cause) {
        super(cause);
    }
}
