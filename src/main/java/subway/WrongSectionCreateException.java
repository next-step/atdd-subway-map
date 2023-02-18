package subway;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class WrongSectionCreateException extends ResponseStatusException {
    public WrongSectionCreateException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }
}
