package subway.exception;

public class StationNotFoundException extends RuntimeException {

    public StationNotFoundException() {
    }

    public StationNotFoundException(String message) {
        super(message);
    }

    public StationNotFoundException(Throwable cause) {
        super(cause);
    }
}
