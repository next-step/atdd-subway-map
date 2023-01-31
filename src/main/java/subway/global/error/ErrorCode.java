package subway.global.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    NOT_FOUND_STATION("해당 지하철역을 찾을 수 없습니다.");
    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}