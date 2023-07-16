package subway.exception.error;

import org.springframework.http.HttpStatus;

public enum LineErrorCode {
    LINE_NOT_FOUND(HttpStatus.BAD_REQUEST, "노선을 찾을 수 없습니다. (line id = %s)");

    private HttpStatus statusCode;
    private String message;

    LineErrorCode(HttpStatus statusCode, String message, Object... messageArgs) {
        this.statusCode = statusCode;
        this.message = String.format(message, messageArgs);
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
