package subway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> handleHttpException(HttpException exception) {
        return new ResponseEntity<>(exception.getErrorCode().getMessage(), exception.getErrorCode().getHttpStatus());
    }

}
