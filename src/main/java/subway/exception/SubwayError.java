package subway.exception;

import org.springframework.http.HttpStatus;

public enum SubwayError {

    NOT_FOUND_LINE(HttpStatus.BAD_REQUEST, "노선정보를 찾을 수 없습니다."),
    NOT_FOUND_STATION(HttpStatus.BAD_REQUEST, "역정보를 찾을 수 없습니다."),
    NAME_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "이름정보가 유효하지 않습니다."),
    COLOR_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "색정보가 유효하지 않습니다."),
    ;

    SubwayError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    private HttpStatus status;
    private String message;

}
