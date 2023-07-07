package subway.global.error.code;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    NOT_EXIST_STATION("지하철역이 존재하지 않습니다."),
    NOT_EXIST_LINE("지하철 노선이 존재하지 않습니다.");

    private String message;

}
