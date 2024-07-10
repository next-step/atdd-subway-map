package subway;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    MISSING_ID(HttpStatus.BAD_REQUEST, "해당 id 로 data 를 찾을 수 없습니다.");
    private HttpStatus httpStatus;
    private String message;

    ErrorCode(HttpStatus status, String message) {
        this.httpStatus = status;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
