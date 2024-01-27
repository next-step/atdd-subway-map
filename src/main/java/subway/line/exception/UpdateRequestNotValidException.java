package subway.line.exception;


public class UpdateRequestNotValidException extends RuntimeException {
    public UpdateRequestNotValidException(final String message) {
        super(message);
    }
}
