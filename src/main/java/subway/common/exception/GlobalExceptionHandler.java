package subway.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // TODO: BaseResponse 두고 거기다가 모든 값 null 이런 패턴을 사용해야 한다고 생각하나, 미션 단순화를 위해 이렇게 둠
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException e) {
        return ResponseEntity.ok().body(new BaseExceptionResponse(e.getMessage()));
    }
}
