package nextstep.subway.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, "-1001", "Resource Not Found"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "-10002", "Bad Request"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "-10003", "Internal Server Error"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "-10004", "Validation Error"),
    NOT_FOUND_LINE_ERROR(HttpStatus.BAD_REQUEST, "-10005", "노선을 찾을 수 없습니다."),
    DUPLICATE_ERROR(HttpStatus.BAD_REQUEST, "-10006", "이미 등록된 정보입니다."),
    NOT_FOUND_STATION_ERROR(HttpStatus.BAD_REQUEST, "-10007", "지하철 역을 찾을 수 없습니다."),
    SECTION_CREATE_ERROR(HttpStatus.BAD_REQUEST, "-10008", "구간을 등록할 수 없습니다."),
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
