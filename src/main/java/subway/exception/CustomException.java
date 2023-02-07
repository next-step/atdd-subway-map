package subway.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomException extends  RuntimeException  {
    private final ErrorCode errorCode;

//    public CustomException(ErrorCode errorCode, String message) {
//        this.errorCode = errorCode;
//        this.message = message;
//    }
}
