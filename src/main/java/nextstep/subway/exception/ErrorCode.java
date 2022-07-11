package nextstep.subway.exception;

import org.springframework.http.HttpStatus;


public enum ErrorCode {
    INVALID_SIZE_SECTIONS(HttpStatus.BAD_REQUEST, "최소 1개 이상의 Section이 필요합니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
