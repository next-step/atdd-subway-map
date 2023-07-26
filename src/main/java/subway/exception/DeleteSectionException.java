package subway.exception;

public class DeleteSectionException extends RuntimeException {
    public DeleteSectionException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
    }
}
