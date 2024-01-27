package subway.line.exception;


public class CreateRequestNotValidException extends RuntimeException {
    public CreateRequestNotValidException(final String message) {
        super(message);
    }
}
