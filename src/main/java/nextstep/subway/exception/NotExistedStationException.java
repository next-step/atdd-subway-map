package nextstep.subway.exception;

public class NotExistedStationException extends IllegalArgumentException {
    public NotExistedStationException(String message) {
        super(message);
    }
}
