package subway.line.exception;

import subway.exceptions.BaseException;

public class NonExistentLineException extends BaseException {

    public NonExistentLineException(final String message) {
        super(message);
    }

}
