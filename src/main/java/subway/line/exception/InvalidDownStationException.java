package subway.line.exception;

import subway.common.exception.SubwayException;
import subway.common.exception.SubwayExceptionType;

public class InvalidDownStationException extends SubwayException {
    public InvalidDownStationException(Long stationId) {
        super(SubwayExceptionType.INVALID_DOWN_STATION, String.format(SubwayExceptionType.INVALID_DOWN_STATION.getMessage(), stationId));
    }
}
