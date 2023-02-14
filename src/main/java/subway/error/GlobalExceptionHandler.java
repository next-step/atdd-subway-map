package subway.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NoSuchElementException.class, IllegalStateException.class})
    public ResponseEntity handleNoSuchElementException(Exception ex){
        return ResponseEntity.badRequest().build();
    }
}
