package nextstep.subway.error.exception;

public class InvalidValueException extends BusinessException{
    public InvalidValueException() {
        super(ErrorCode.INVALID_INPUT_VALUE);
    }
    public InvalidValueException(ErrorCode errorCode) {
        super(errorCode);
    }
}
