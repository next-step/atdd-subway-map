package nextstep.subway.domain.exception;

import org.springframework.http.HttpStatus;

public class DomainException extends RuntimeException {

    private final String message;
    private final HttpStatus status;

    public DomainException(DomainExceptionType type) {
        this.message = type.getMessage();
        this.status = type.getStatus();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
