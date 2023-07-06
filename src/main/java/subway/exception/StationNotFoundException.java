package subway.exception;

import static subway.exception.SubwayError.NOT_FOUND_STATION;

public class StationNotFoundException extends SubwayException {
    public StationNotFoundException() {
        super(NOT_FOUND_STATION);
    }
}
