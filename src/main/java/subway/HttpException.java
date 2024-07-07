package subway;

public class HttpException extends Exception {
    private ErrorCode errorCode;

    public HttpException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
