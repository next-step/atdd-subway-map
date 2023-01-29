package subway.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(value = {NotFoundLineException.class})
    public ResponseEntity<?> notFoundException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<?> badRequestException() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<?> exception() {
        return ResponseEntity.internalServerError().build();
    }
}
