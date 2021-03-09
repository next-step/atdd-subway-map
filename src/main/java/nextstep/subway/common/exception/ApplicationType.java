package nextstep.subway.common.exception;

import org.springframework.http.HttpStatus;

public enum ApplicationType {
    CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND),
    INVALID_REQUEST_PARAMETER(HttpStatus.BAD_REQUEST),
    INVALID_ID(HttpStatus.BAD_REQUEST),
    KEY_DUPLICATED(HttpStatus.BAD_REQUEST);

    private final HttpStatus httpStatus;

    ApplicationType(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
}
