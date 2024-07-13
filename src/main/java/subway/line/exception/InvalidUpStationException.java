package subway.line.exception;

import subway.exceptions.BaseException;

public class InvalidUpStationException extends BaseException {
    public InvalidUpStationException(final String message) {
        super(message);
    }
}
