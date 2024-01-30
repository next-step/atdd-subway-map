package subway.line.exception;

import subway.common.exception.BadRequestException;

public class SectionConnectException extends BadRequestException {
    public SectionConnectException(final String message) {
        super(message);
    }
}
