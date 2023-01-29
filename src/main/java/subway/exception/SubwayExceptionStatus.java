package subway.exception;

import org.springframework.http.HttpStatus;

public enum SubwayExceptionStatus {
    STATION_NOT_FOUND(HttpStatus.NOT_FOUND),
    LINE_NOT_FOUND(HttpStatus.NOT_FOUND);

    private final HttpStatus status;

    SubwayExceptionStatus(HttpStatus status) {
        this.status = status;
    }

    public HttpStatus getHttpStatus() {
        return status;
    }
}
