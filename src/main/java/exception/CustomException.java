package exception;

public class CustomException extends RuntimeException{
    private final ErrorCode errorCode;

    // 생성자
    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    // getter 메서드
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
