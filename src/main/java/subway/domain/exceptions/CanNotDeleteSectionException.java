package subway.domain.exceptions;

public class CanNotDeleteSectionException extends RuntimeException {
    public CanNotDeleteSectionException(String message) {
        super(message);
    }
}
