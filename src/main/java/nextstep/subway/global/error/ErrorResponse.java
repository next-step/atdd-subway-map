package nextstep.subway.global.error;

import nextstep.subway.global.error.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class ErrorResponse {

    private final HttpStatus status;
    private final String message;

    private ErrorResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode.getStatus(), errorCode.getMessage());
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
