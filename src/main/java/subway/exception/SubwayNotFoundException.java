package subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class SubwayNotFoundException extends RuntimeException {
        public SubwayNotFoundException(String message) { super(message); }
}
