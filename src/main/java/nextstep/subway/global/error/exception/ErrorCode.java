package nextstep.subway.global.error.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    DUPLICATION_STATION_NAME(HttpStatus.FORBIDDEN, "duplication station name"),
    DUPLICATION_LINE_NAME(HttpStatus.FORBIDDEN, "duplication line name"),
    NOT_FOUND_LINE(HttpStatus.NOT_FOUND, "not found line");

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
