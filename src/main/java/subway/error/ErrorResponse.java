package subway.error;

import subway.error.exception.ErrorCode;

public class ErrorResponse {

    private final String message;

    public ErrorResponse(final ErrorCode errorCode) {
        this.message = errorCode.getMessage();
    }

    public String getMessage() {
        return message;
    }
}
