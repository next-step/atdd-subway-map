package subway.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SubwayException extends RuntimeException {
    private final ErrorCode errorCode;
}
