package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import subway.common.NotFoundException;

@ControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<Void> notFound(Exception ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = {IllegalStateException.class})
    public ResponseEntity<Void> badRequest(Exception ex) {
        return ResponseEntity.badRequest().build();
    }
}
