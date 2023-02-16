package subway.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    NOT_FOUND_STATION(HttpStatus.NOT_FOUND, "지하철 노선에 등록된 역이어야 한다."),
    END_STATION_MISMATCH(HttpStatus.BAD_REQUEST, "새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다."),
    DUPLICATED_STATION(HttpStatus.BAD_REQUEST, "하행역이 해당 노선에 등록되어 있지 않는 역인 구간이어야 한다."),
    NOT_END_STATION(HttpStatus.BAD_REQUEST, "하행 종점역만 제거할 수 있다."),
    INSUFFICIENT_SECTION(HttpStatus.BAD_REQUEST, "구간이 2개 이상인 지하철 노선이어야 한다.");

    private final HttpStatus httpStatus;
    private final String message;


    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
