package subway.line;

import subway.exception.BusinessException;
import subway.exception.ErrorMessage;

public class MismatchedUpstreamStationException extends BusinessException {

    private static final ErrorMessage ERROR_MESSAGE = ErrorMessage.MISMATCHED_UPSTREAM_STATION_EXCEPTION;

    public MismatchedUpstreamStationException() {
        super(ERROR_MESSAGE.getMessage());
    }
}
