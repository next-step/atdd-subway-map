package nextstep.subway.exception;

import org.springframework.http.HttpStatus;


public enum ErrorCode {
    INVALID_SIZE_SECTIONS(HttpStatus.BAD_REQUEST, "최소 1개 이상의 Section이 필요합니다."),
    INVALID_UP_STATION_EXCEPTION(HttpStatus.BAD_REQUEST, "신규 등록되는 구간의 상행역은 기존 노선의 하행역만 가능합니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
