package nextstep.subway.exception;

public class NotFoundRequestException extends RuntimeException {

    public NotFoundRequestException() {
    }

    public NotFoundRequestException(String message) {
        super(message);
    }
}
