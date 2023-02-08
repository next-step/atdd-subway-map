package subway.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.exception.*;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Void> noSuchElement() {
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ExtendSectionException.class)
    public ResponseEntity<String> extendSectionException(ExtendSectionException e) {
        return ResponseEntity
                .badRequest()
                .body(e.getMessage());
    }

    @ExceptionHandler(ReduceSectionException.class)
    public ResponseEntity<String> reduceSectionException(ReduceSectionException e) {
        return ResponseEntity
                .badRequest()
                .body(e.getMessage());
    }

}
