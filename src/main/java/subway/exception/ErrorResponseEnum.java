package subway.exception;

import org.springframework.http.HttpStatus;

public enum ErrorResponseEnum {
    ERROR_NO_FOUND_LINE (HttpStatus.INTERNAL_SERVER_ERROR,"do not found line by id"),
    ERROR_UPSTATION_INVAILD_LINE (HttpStatus.INTERNAL_SERVER_ERROR, "upStation's not equal (last down station)"),
    ERROR_DOWNSTATION_INVAILD_LINE (HttpStatus.INTERNAL_SERVER_ERROR, "downStation's already exists"),
    ERROR_DELETE_SECTION_COUNT_LINE (HttpStatus.INTERNAL_SERVER_ERROR, "line have only one section"),
    ERROR_DELETE_SECTION_NO_LAST_SECTION_LINE (HttpStatus.INTERNAL_SERVER_ERROR, "section's not last section"),
    ERROR_NO_FOUND_SECTION (HttpStatus.INTERNAL_SERVER_ERROR, "do not found section by id");

    HttpStatus httpStatus;
    String message;

    ErrorResponseEnum(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    public String getMessage() {
        return this.message;
    }
}
