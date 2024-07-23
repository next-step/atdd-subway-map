package subway.common.exception;

import subway.common.response.ErrorCode;

public class InvalidDownStationException extends BusinessException {
    public InvalidDownStationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
