package nextstep.subway.exception;

public class NotExistedStationException extends IllegalArgumentException {
    public NotExistedStationException() {
        super();
    }

    public NotExistedStationException(String message) {
        super(message);
    }

}
