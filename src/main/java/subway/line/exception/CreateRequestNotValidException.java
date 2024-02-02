package subway.line.exception;


import subway.common.exception.ValidationError;

public class CreateRequestNotValidException extends ValidationError {
    public CreateRequestNotValidException(final String message) {
        super(message);
    }
}
