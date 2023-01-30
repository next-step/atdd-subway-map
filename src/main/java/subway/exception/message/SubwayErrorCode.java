package subway.exception.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

import subway.exception.ErrorCode;

@Getter
@AllArgsConstructor
public enum SubwayErrorCode implements ErrorCode {

    NOT_FOUND_STATION("존재하지 않는 역입니다.");

    String message;
}
