package subway.global.error.exception;

import org.springframework.http.HttpStatus;
import subway.global.error.code.ErrorCode;

public class InvalidLineSectionException extends BusinessException {

    public InvalidLineSectionException(ErrorCode errorCode) {
        super(HttpStatus.BAD_REQUEST.value(), errorCode.getMessage());
    }

}
