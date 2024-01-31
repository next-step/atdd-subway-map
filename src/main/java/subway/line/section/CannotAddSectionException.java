package subway.line.section;

import org.springframework.http.HttpStatus;

public class CannotAddSectionException extends Exception{
    public CannotAddSectionException() {
        super();
    }

    public CannotAddSectionException(String message) {
        super(message);
    }
}
