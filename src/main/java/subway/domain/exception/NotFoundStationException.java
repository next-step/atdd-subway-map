package subway.domain.exception;

public class NotFoundStationException extends RuntimeException {

    public NotFoundStationException() {
        super("Station을 찾지못했습니다");
    }

    public NotFoundStationException(String message) {
        super(message);
    }

}
