package subway.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.common.exception.NotFoundException;

@RestControllerAdvice
public class ErrorAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> notFoundEntity(NotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
