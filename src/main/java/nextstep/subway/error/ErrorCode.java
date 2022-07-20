package nextstep.subway.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    NOT_STATION_FOUND(HttpStatus.NOT_FOUND.value(), "NOT_STATION_FOUND"),
    NOT_LINE_FOUND(HttpStatus.NOT_FOUND.value(), "NOT_LINE_FOUND");

    private int status;
    private String errorCode;

    ErrorCode(int status, String errorCode) {
        this.status = status;
        this.errorCode = errorCode;
    }

    public int getStatus() {
        return status;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
