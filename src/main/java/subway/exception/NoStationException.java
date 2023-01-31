package subway.exception;

public class NoStationException extends RuntimeException {
    public NoStationException() {
        super("해당 역이 존재하지 않습니다.");
    }
}
