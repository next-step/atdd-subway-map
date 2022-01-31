package nextstep.subway.exception;

import org.springframework.http.HttpStatus;

public enum LogicError {
    DUPLICATED_NAME_LINE("Line name is duplicated", HttpStatus.BAD_REQUEST),
    DUPLICATED_NAME_STATION("Station name is duplicated", HttpStatus.BAD_REQUEST),
    NOT_EXIST_LINE("Does not exist line", HttpStatus.NO_CONTENT),
    NOT_EXIST_STATION("Does not exist station", HttpStatus.NO_CONTENT);

    private final String error;
    private final HttpStatus httpStatus;

    LogicError(String error, HttpStatus httpStatus) {
        this.error = error;
        this.httpStatus = httpStatus;
    }

    public String getError() {
        return error;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}