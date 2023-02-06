package subway.exception;

import org.springframework.http.HttpStatus;

public enum ErrorResponseCode {

    NOT_FOUND_STATION(HttpStatus.NOT_FOUND, "해당하는 id의 지하철 역이 존재하지 않습니다."),
    NOT_FOUND_LINE(HttpStatus.NOT_FOUND, "해당하는 id의 지하철 노선이 존재하지 않습니다."),
    NOT_EQUAL_LAST_STATION(HttpStatus.BAD_REQUEST, "새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 합니다."),
    DUPLICATED_DOWN_STATION(HttpStatus.BAD_REQUEST, "새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없습니다."),
    NOT_THE_LAST_STATION(HttpStatus.BAD_REQUEST, "지하철 노선에 등록된 하행 종점역만 제거할 수 있습니다."),
    SINGLE_SECTION_ERROR(HttpStatus.BAD_REQUEST, "지하철 노선에 구간이 1개인 경우에는 역을 삭제할 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorResponseCode(HttpStatus httpStatus, String message) {
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
