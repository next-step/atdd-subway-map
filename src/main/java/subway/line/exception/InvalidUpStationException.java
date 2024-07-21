package subway.line.exception;

import subway.common.exception.SubwayException;
import subway.common.exception.SubwayExceptionType;

public class InvalidUpStationException extends SubwayException {
    public InvalidUpStationException(Long stationId) {
        super(SubwayExceptionType.INVALID_UP_STATION, String.format(SubwayExceptionType.INVALID_UP_STATION.getMessage(), stationId));
    }
}
