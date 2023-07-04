package subway.line;

import subway.exception.BusinessException;
import subway.exception.ErrorCode;

public class MismatchedUpstreamStationException extends BusinessException {

    private static final ErrorCode errorCode = ErrorCode.MISMATCHED_UPSTREAM_STATION_EXCEPTION;

    public MismatchedUpstreamStationException() {
        super(errorCode.getMessage());
    }
}
