package nextstep.subway.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    NOT_STATION_FOUND(HttpStatus.NOT_FOUND.value(), "NOT_STATION_FOUND"),
    NOT_LINE_FOUND(HttpStatus.NOT_FOUND.value(), "NOT_LINE_FOUND"),
    MISMATCH_UPPER_SECTION_WITH_LOWER_LINE(HttpStatus.INTERNAL_SERVER_ERROR.value(), "MISMATCH_BETWEEN_UPPER_STATION_OF_SECTION_AND_LOWER_STATION_OF_LINE"),
    DUPLICATE_STATION_ERR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "FAIL_TO_ADD_DUPLICATE_STATION");

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
