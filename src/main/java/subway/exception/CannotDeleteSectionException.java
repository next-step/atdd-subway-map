package subway.exception;

public class CannotDeleteSectionException extends RuntimeException {

    public CannotDeleteSectionException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
    }
}
