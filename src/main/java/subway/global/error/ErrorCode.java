package subway.global.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    NOT_FOUND_STATION("해당 지하철역을 찾을 수 없습니다."),
    NOT_FOUND_LINE("해당 노선을 찾을 수 없습니다."),

    SECTION_MUST_BE_ADDED_END_OF_LINE("구간은 노선의 맨 마지막에만 등록 가능합니다."),
    ALREADY_EXIST_STATION("이미 노선에 존재하는 역입니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}