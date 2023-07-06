package subway.exception;

import static subway.exception.SubwayError.NAME_NOT_AVAILABLE;

public class NameNotAvailableException extends SubwayException {
    public NameNotAvailableException() {
        super(NAME_NOT_AVAILABLE);
    }
}
