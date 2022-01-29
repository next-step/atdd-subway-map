package nextstep.subway.global.error.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    DUPLICATION_STATION_NAME(HttpStatus.CONFLICT, "duplication station name"),
    DUPLICATION_LINE_NAME(HttpStatus.CONFLICT, "duplication line name"),
    NOT_FOUND_LINE(HttpStatus.NOT_FOUND, "not found line"),
    ILLEGAL_ARGUMENT(HttpStatus.BAD_REQUEST, "argument is unsuitable"),
    ILLEGAL_SECTION_UPSTATION(HttpStatus.CONFLICT, "section upstation must be down station on line");

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
