package subway.commons;

import org.springframework.http.HttpStatus;

import java.text.MessageFormat;

public enum ErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    MISSING_ID(HttpStatus.BAD_REQUEST, "해당 id:{0} 로 data 를 찾을 수 없습니다."),
    UP_STATION_NOT_VALID(HttpStatus.BAD_REQUEST, "기존 하행종점역이 반드시 상행역으로 등록되어야 합니다"),
    DOWN_STATION_NOT_VALID(HttpStatus.BAD_REQUEST, "이미 등록되어 있는 역(id:{0})은 새로운 하행종점역으로 등록할 수 없습니다."),
    IS_NOT_TERMINAL_STATION(HttpStatus.BAD_REQUEST, "지금 제거하는 역(id:{0})은 하행종점역이 아닙니다."),
    CANNOT_REMOVE_LAST_SECTION(HttpStatus.BAD_REQUEST, "마지막 구간은 제거가 불가능 합니다.");
    private HttpStatus httpStatus;
    private String message;

    ErrorCode(HttpStatus status, String message) {
        this.httpStatus = status;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
