package subway.line.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StationMessage {

    NOT_FOUND_MESSAGE("존재하지 않는 역 입니다");

    private final String message;
}
