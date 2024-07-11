package subway;

public class HttpException extends RuntimeException {
    private ErrorCode errorCode;

    public HttpException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
