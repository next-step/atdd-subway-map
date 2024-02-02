package subway.line.exception;


import subway.common.exception.ValidationError;

public class UpdateRequestNotValidException extends ValidationError {
    public UpdateRequestNotValidException(final String message) {
        super(message);
    }
}
