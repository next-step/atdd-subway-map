package subway.line.exception;

import subway.exceptions.BaseException;

public class InsufficientStationsException extends BaseException {
    public InsufficientStationsException(final String message) {
        super(message);
    }
}
