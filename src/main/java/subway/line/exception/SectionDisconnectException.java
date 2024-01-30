package subway.line.exception;

import subway.common.exception.BadRequestException;

public class SectionDisconnectException extends BadRequestException {
    public SectionDisconnectException(final String message) {
        super(message);
    }
}
