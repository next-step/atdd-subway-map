package subway.exception;

import org.springframework.http.HttpStatus;

public enum SubwayExceptionStatus {
    STATION_NOT_FOUND(HttpStatus.NOT_FOUND, "지하철역을 찾을 수 없습니다."),
    LINE_NOT_FOUND(HttpStatus.NOT_FOUND, "노선을 찾을 수 없습니다.");

    private final HttpStatus status;
    private final String message;

    SubwayExceptionStatus(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
