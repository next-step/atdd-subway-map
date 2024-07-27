package subway.global.exception;

public class BadRequestException extends RuntimeException {

    private final int code;
    private final String message;

    public BadRequestException(ExceptionCode exceptionCode) {
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
