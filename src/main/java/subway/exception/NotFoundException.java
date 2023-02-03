package subway.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
    }
}
