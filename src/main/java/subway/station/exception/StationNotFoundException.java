package subway.station.exception;

import subway.common.exception.BusinessException;
import subway.common.exception.ErrorCode;

public class StationNotFoundException extends BusinessException {
    public StationNotFoundException() {
        super(ErrorCode.STATION_NOT_FOUND_EXCEPTION);
    }
}
