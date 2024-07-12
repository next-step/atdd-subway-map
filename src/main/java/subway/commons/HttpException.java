package subway.commons;

public class HttpException extends RuntimeException {
    private ErrorCode errorCode;

    private String field;

    public HttpException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public HttpException(ErrorCode errorCode, String field) {
        this.errorCode = errorCode;
        this.field = field;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public String getField() {
        return field;
    }
}
