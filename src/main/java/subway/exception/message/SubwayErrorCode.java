package subway.exception.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SubwayErrorCode {

    NOT_FOUND_STATION("존재하지 않는 역입니다."),
    SECTION_SAME_STATION("상행역과 하행역이 같은 구간은 생성할 수 없습니다."),
    SECTION_DELETE_ERROR("구간이 하나인 노선에서는 역을 제거할 수 없습니다."),
    SECTION_NOT_FOUND("구간이 존재하지 않습니다."),
    ONLY_LAST_SEGMENT_CAN_BE_REMOVED("마지막 구간만 제거할 수 있습니다."),

    DOWN_STATION_HAS_BEEN_REGISTERED("새로운 구간의 하행역이 해당 노선에 등록되어있습니다."),
    STATION_UPPER_SECTION("새로운 구간의 상행역은 해당 노선의 하행 종점역이어야 합니다.");

    String message;
    HttpStatus httpStatus;

    SubwayErrorCode(final String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }
}
