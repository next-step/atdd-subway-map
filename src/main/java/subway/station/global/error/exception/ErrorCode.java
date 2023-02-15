package subway.station.global.error.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    MISMATCHED_DOWN_STATION_OF_LINE(HttpStatus.BAD_REQUEST.value(), "새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다"),
    ALREADY_REGISTERED_IN_LINE(HttpStatus.BAD_REQUEST.value(), "새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다"),
    NOT_STATION_OF_END_SECTION(HttpStatus.BAD_REQUEST.value(), "지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다"),
    STATION_NOT_EXISTS_IN_LINE(HttpStatus.BAD_REQUEST.value(), "검색된 지하철 역이 노선에 존재하지 않습니다. id를 바꿔주세요"),
    NOT_EXISTS_STATION(HttpStatus.BAD_REQUEST.value(), "검색된 지하철이 없습니다. id를 바꿔주세요"),
    STATION_NOT_EXISTS_SECTION(HttpStatus.BAD_REQUEST.value(), "검색된 지하철 역이 구간에 존재하지 않습니다. id를 바꿔주세요"),
    LINE_HAS_ONLY_ONE_SECTION(HttpStatus.BAD_REQUEST.value(), "지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다"),
    SECTION_IS_NOT_END_OF_LINE(HttpStatus.BAD_REQUEST.value(), "지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다.");

    private int status;
    private String errorMessage;

    ErrorCode(int status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }
}
