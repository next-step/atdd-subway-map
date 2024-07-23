package subway.common.exception;

import subway.common.response.ErrorCode;

public class InvalidUpSationException extends BusinessException {
    public InvalidUpSationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
