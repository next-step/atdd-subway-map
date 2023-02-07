package subway.domain.exception;

public class NotFoundLineException extends RuntimeException {

    public NotFoundLineException() {
        super("Line을 찾지못했습니다");
    }

    public NotFoundLineException(String message) {
        super(message);
    }

}
