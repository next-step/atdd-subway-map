package nextstep.subway.applicaion.exception;

import org.springframework.http.HttpStatus;

public class ElementDuplicateException extends RuntimeException{
    private final HttpStatus statusCode = HttpStatus.CONFLICT;
    private final String reason;

    public ElementDuplicateException(String reason) {
        super(reason);
        this.reason = reason;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public String getReason() {
        return reason;
    }
}
