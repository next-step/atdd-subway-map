package subway.exception;

import org.springframework.http.HttpStatus;
import subway.exception.error.LineErrorCode;

public class LineException extends RuntimeException {

    private HttpStatus statusCode;

    public LineException(LineErrorCode errorCode) {
        super(errorCode.getMessage());
        this.statusCode = errorCode.getStatusCode();
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

}
