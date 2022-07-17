package nextstep.subway.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INVALID_UP_STATION_EXCEPTION(HttpStatus.BAD_REQUEST, "상행역은 하행종점역이어야 합니다."),
    ALREADY_CONTAINS_STATION_EXCEPTION(HttpStatus.BAD_REQUEST,  "이미 등록된 역입니다."),
    DELETE_SECTION_DENIED_EXCEPTION(HttpStatus.BAD_REQUEST,  "마지막 구간이 아닙니다."),
    INVALID_SIZE_SECTION_EXCEPTION(HttpStatus.BAD_REQUEST,  "구간이 1개일 때는 구간 제거가 불가능합니다.")
    ;

    private final HttpStatus status;
    private final String message;

    ErrorCode(final HttpStatus status, final String message) {
        this.status = status;
        this.message = message;
    }
}
