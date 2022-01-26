package nextstep.subway.exception;

import nextstep.subway.error.ErrorCode;
import org.springframework.validation.Errors;

public class ValidationException extends NextStepException {

    private final Errors errors;

    public ValidationException(final Errors errors) {
        super(ErrorCode.VALIDATION_ERROR);
        this.errors = errors;
    }

    public ValidationException(final String message) {
        super(ErrorCode.VALIDATION_ERROR, message);
        this.errors = null;
    }

    public Errors getErrors() {
        return errors;
    }
}
