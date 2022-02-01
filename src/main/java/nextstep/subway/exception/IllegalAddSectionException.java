package nextstep.subway.exception;

import static nextstep.subway.exception.Messages.ILLEGAL_ADD_SECTION;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalAddSectionException extends RuntimeException {
    public IllegalAddSectionException() {
        super(ILLEGAL_ADD_SECTION.message());
    }
}
