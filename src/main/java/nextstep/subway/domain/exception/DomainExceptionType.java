package nextstep.subway.domain.exception;

import org.springframework.http.HttpStatus;

public enum DomainExceptionType {

    DONT_MATCH_STATION_BETWEEN_SECTIONS("두 구간의 상/하행역이 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    CAN_NOT_REMOVE_SECTION_IN_MIDDLE("마지막 구간이 아닌 구간은 제거할 수 없습니다.", HttpStatus.BAD_REQUEST),
    CAN_NOT_ADD_DUPLICATE_STATION("구간에 이미 존재하는 역은 등록할 수 없습니다", HttpStatus.BAD_REQUEST),
    CAN_NOT_REMOVE_LAST_SECTION("노선에는 하나 이상의 구간이 존재해야 합니다.", HttpStatus.BAD_REQUEST)
    ;

    private final String message;
    private final HttpStatus status;

    DomainExceptionType(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
