package subway.line.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    ADD_UP_STATION_IS_NOT_FINAL_STATION(HttpStatus.BAD_REQUEST, "등록하고자 하는 구간의 상행역이 현재 노선의 하행종점역이 아니면 구간을 등록할 수 없습니다."),
    ADD_DOWN_STATION_IN_LINE(HttpStatus.BAD_REQUEST, "등록하고자 하는 구간의 하행역이 현재 노선에 이미 포함되어 있다면 구간을 등록할 수 없습니다."),
    SECTION_NOT_EMPTY(HttpStatus.BAD_REQUEST, "현재 구간 갯수가 하나이므로 구간을 삭제할 수 없습니다."),
    REMOVE_STATION_MUST_BE_FINAL_STATION(HttpStatus.BAD_REQUEST, "하행종점역이 아닌 역은 삭제할 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
