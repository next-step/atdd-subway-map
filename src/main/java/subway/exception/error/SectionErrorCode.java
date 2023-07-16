package subway.exception.error;

import org.springframework.http.HttpStatus;

public enum SectionErrorCode {

    NO_MATCH_UP_STATION(HttpStatus.BAD_REQUEST, "구간의 상행역과 노선의 하행 종점역이 일치하지 않습니다."),
    ALREADY_EXIST_DOWN_STATION(HttpStatus.BAD_REQUEST, "구간의 하행역이 노선에 이미 등록되어있습니다."),
    NON_LAST_STATION_DELETE_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "마지막 구간만 삭제할 수 있습니다."),
    SINGLE_SECTION_DELETE_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "단일 구간 노선의 구간은 삭제할 수 없습니다."),
    CANNOT_CREATE_SECTION(HttpStatus.BAD_REQUEST, "구간을 생성할 수 없습니다.")
    ;

    private HttpStatus statusCode;
    private String message;

    SectionErrorCode(HttpStatus statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
