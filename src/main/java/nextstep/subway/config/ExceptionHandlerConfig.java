package nextstep.subway.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class ExceptionHandlerConfig {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> entityNotFoundExceptionHandler(EntityNotFoundException exception) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Void> illegalStateException(IllegalStateException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
