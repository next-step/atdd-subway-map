package subway.exception;

import org.springframework.http.HttpStatus;

public class SubwayException extends RuntimeException {

    private final SubwayExceptionStatus status;

    public SubwayException(String message) {
        this(message, SubwayExceptionStatus.ERROR);
    }

    public SubwayException(SubwayExceptionStatus status) {
        this(status.getMessage(), status);
    }

    public SubwayException(String message, SubwayExceptionStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status.getHttpStatus();
    }
}
