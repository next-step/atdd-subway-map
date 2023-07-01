package subway.exception;

public class LineNotEstablishedBySameEndStationException extends RuntimeException {

    public LineNotEstablishedBySameEndStationException() {
    }

    public LineNotEstablishedBySameEndStationException(String message) {
        super(message);
    }

    public LineNotEstablishedBySameEndStationException(Throwable cause) {
        super(cause);
    }
}
