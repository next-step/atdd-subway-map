package subway.line;

import subway.exception.BusinessException;
import subway.exception.ErrorMessage;

public class DownstreamStationIncludedException extends BusinessException {

    private static final ErrorMessage ERROR_MESSAGE = ErrorMessage.DOWNSTREAM_STATION_INCLUDED_EXCEPTION;

    public DownstreamStationIncludedException() {
        super(ERROR_MESSAGE.getMessage());
    }
}
