package subway.exception;

public class SubwayException extends RuntimeException {

    protected final ErrorCode errorCode;

    public SubwayException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
