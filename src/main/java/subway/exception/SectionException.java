package subway.exception;

import org.springframework.http.HttpStatus;
import subway.exception.error.SectionErrorCode;

public class SectionException extends RuntimeException {

    private HttpStatus statusCode;
    private String message;

    public SectionException(SectionErrorCode errorCode) {
        super(errorCode.getMessage());
        this.statusCode = errorCode.getStatusCode();
        this.message = errorCode.getMessage();
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
