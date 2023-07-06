package subway.global.error.exception;

import subway.global.error.code.ErrorCode;

public class NotEntityFoundException extends BusinessException {

    public NotEntityFoundException(ErrorCode errorCode) {
        super(404, errorCode.getMessage());
    }

}
