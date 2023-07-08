package subway.exception;

import lombok.Getter;

public class SubwayNotFoundException extends SubwayException {
    public SubwayNotFoundException(final String message) {
        super(message);
    }
}
