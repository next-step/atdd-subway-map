package subway.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<Void> handle(Exception ex) {
        return ResponseEntity.noContent().build();
    }
}
