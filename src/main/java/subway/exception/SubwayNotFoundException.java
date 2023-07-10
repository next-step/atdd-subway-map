package subway.exception;

import lombok.Getter;

public class SubwayNotFoundException extends SubwayException {
    public SubwayNotFoundException(final long code, final String message) {
        super(code, message);
    }
}
