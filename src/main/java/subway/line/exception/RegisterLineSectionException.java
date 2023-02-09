package subway.line.exception;

public class RegisterLineSectionException extends RuntimeException {
    private final ErrorCode errorCode;

    public RegisterLineSectionException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
