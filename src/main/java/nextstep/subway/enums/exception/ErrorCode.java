package nextstep.subway.enums.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    NOT_FOUND_STATION(404, "STATION-ERR", "해당 역을 찾을 수 없습니다."),
    SAME_STATION(500, "SECTION-ERR", "기존 노선의 하행종점역과 새로 등록하려는 상행역은 같아야합니다."),
    NOT_SAME_STATION(500, "SECTION-ERR", "상행역종점역과 하행역종점역은 같을 수 없습니다."),
    NOT_FOUND_SECTION(404, "SECTION-ERR", "저장된 구간이 없습니다."),
    ALREADY_REGISTER_STATION(500, "DUPLICATE-ERR", "새로운 구간의 하행역은 해당 노선에 이미 등록되어있습니다."),
    NOT_FOUND_LINE(404, "LINE-ERR", "해당 노선은 없습니다."),
    NOT_ENOUGH_SECTION(500, "SECTION-ERR", "구간이 1개 밖에 없습니다.");


    private final int status;
    private final String errorCode;
    private final String message;
}
