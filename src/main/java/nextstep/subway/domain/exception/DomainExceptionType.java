package nextstep.subway.domain.exception;

import org.springframework.http.HttpStatus;

public enum DomainExceptionType {

    DONT_MATCH_STATION_BETWEEN_SECTIONS("두 구간의 상/하행역이 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    CAN_NOT_REMOVE_SECTION_IN_MIDDLE("마지막 구간이 아닌 구간은 제거할 수 없습니다.", HttpStatus.BAD_REQUEST)
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
