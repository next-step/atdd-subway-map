package exception;

import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class ErrorResponse {
    private final LocalDateTime timestamp;
    private final int status;
    private final String error;
    private final String message;

    // 생성자
    public ErrorResponse(ErrorCode errorCode) {
        this.timestamp = LocalDateTime.now();
        this.status = errorCode.getStatus().value();
        this.error = errorCode.name();
        this.message = errorCode.getMessage();
    }

    // getter 메서드
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

    // 정적 팩토리 메서드
    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getStatus()).body(new ErrorResponse(errorCode));
    }
}
