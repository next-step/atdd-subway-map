package nextstep.subway.common.exception;

import lombok.Getter;

@Getter
public enum ResponseCode {
    ETC(1000, "알 수 없는 오류입니다."),
    PARAM_INVALID(1001, "올바르지 않은 파라미터입니다."),

    LINE_NOT_FOUND (2000, "노선이 존재하지 않습니다."),
    LINE_STATION_DUPLICATE(2001, "노선에 이미 존재하는 역입니다."),

    STATION_NOT_FOUND(3000, "지하철역이 존재하지 않습니다.");

    private final int code;

    private final String message;

    ResponseCode(int code, String message){
        this.code = code;
        this.message = message;
    }
}
