package subway.station.exception;

import subway.exceptions.BaseException;

public class NonExistentStationException extends BaseException {

    public NonExistentStationException(final String message) {
        super(message);
    }

}
