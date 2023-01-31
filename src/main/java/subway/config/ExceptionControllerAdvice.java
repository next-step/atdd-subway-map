package subway.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Void> noSuchElement() {
        return ResponseEntity.noContent().build();
    }
}
