package nextstep.subway.applicaion.exception;

import org.springframework.http.HttpStatus;

public class DuplicateException extends RuntimeException {
    private final HttpStatus code = HttpStatus.CONFLICT;

    public DuplicateException(String message) {
        super(message);
    }

    public HttpStatus getCode() {
        return code;
    }
}
