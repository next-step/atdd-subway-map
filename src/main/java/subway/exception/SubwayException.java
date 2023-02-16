package subway.exception;

public class SubwayException extends RuntimeException {

    private final ErrorCode errorCode;

    public SubwayException(ErrorCode errorCode) {

        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
