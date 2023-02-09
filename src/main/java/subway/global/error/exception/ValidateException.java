package subway.global.error.exception;

import subway.global.error.ErrorCode;

public class ValidateException extends BusinessException{
    public ValidateException(ErrorCode errorCode) {
        super(errorCode);
    }
}
