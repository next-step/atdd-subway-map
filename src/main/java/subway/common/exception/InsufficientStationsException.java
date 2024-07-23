package subway.common.exception;

import subway.common.response.ErrorCode;

public class InsufficientStationsException extends BusinessException {
    public InsufficientStationsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
