package nextstep.subway.domain.exception;

import org.springframework.http.HttpStatus;

public enum DomainExceptionType {

    ;

    private final String message;
    private final HttpStatus status;

    DomainExceptionType(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
