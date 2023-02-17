package subway;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class WrongSectionDeleteException extends ResponseStatusException {
    public WrongSectionDeleteException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }
}
