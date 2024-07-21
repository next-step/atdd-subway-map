package subway.station.exception;

import subway.common.exception.SubwayException;
import subway.common.exception.SubwayExceptionType;

public class StationNotFoundException extends SubwayException {
    public StationNotFoundException(Long stationId) {
        super(SubwayExceptionType.STATION_NOT_FOUND, String.format(SubwayExceptionType.STATION_NOT_FOUND.getMessage(), stationId));
    }
}
