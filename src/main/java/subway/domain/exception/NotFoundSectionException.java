package subway.domain.exception;

public class NotFoundSectionException extends RuntimeException {

    public NotFoundSectionException() {
        super("Section을 찾지못했습니다");
    }

    public NotFoundSectionException(String message) {
        super(message);
    }

}
