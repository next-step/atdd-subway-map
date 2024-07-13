package subway.line.exception;

import subway.exceptions.BaseException;

public class InvalidDownStationException extends BaseException {
    public InvalidDownStationException(final String message) {
        super(message);
    }
}
