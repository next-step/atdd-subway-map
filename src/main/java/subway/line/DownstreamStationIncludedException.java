package subway.line;

import subway.exception.BusinessException;
import subway.exception.ErrorCode;

public class DownstreamStationIncludedException extends BusinessException {

    private static final ErrorCode errorCode = ErrorCode.DOWNSTREAM_STATION_INCLUDED_EXCEPTION;

    public DownstreamStationIncludedException() {
        super(errorCode.getMessage());
    }
}
