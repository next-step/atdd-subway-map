package nextstep.subway.applicaion.exception;

public class NotFoundException extends RuntimeException{

    public NotFoundException(long id) {
        final String message = String.format("해당하는 대상을 찾을 수 없습니다. id : %s", id);
        new NotFoundException(message);
    }

    public NotFoundException() {
    }

    public NotFoundException(String message) {
        super(message);
    }

    public NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
