package subway.exception;

public class NoLineException extends RuntimeException {
    public NoLineException() {
        super("해당 호선이 존재하지 않습니다.");
    }
}
