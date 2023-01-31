package subway.exception;

import org.springframework.http.HttpStatus;

public class SubwayException extends RuntimeException {
    public SubwayException(String message) {
        super(message);
    }
}
