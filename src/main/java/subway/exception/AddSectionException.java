package subway.exception;

public class AddSectionException extends RuntimeException {
    public AddSectionException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
    }
}
