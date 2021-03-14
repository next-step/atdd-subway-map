package nextstep.subway.common.exception;

public class InvalidRequestException  extends RuntimeException {
    public InvalidRequestException() {
    }

    public InvalidRequestException(String message) {
        super("유효하지 않은 요청입니다. : " + message);
    }
}
