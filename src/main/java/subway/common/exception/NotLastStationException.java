package subway.common.exception;

import subway.common.response.ErrorCode;

public class NotLastStationException extends BusinessException {
    public NotLastStationException(ErrorCode errorCode) {
        super(errorCode);
    }
}
