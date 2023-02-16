package subway.line.web.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.security.InvalidParameterException;
import java.util.NoSuchElementException;

@ControllerAdvice
public class LineControllerAdvice {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> notExist() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(InvalidParameterException.class)
    public ResponseEntity<String> invalidParams(InvalidParameterException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

}
