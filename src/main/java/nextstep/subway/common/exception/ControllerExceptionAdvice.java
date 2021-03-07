package nextstep.subway.common.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionAdvice {

    @ExceptionHandler(ExistResourceException.class)
    public ResponseEntity<Void> existResourceExceptionHandler(RuntimeException e) {
        return ResponseEntity.unprocessableEntity().build();
    }

    @ExceptionHandler(NonExistResourceException.class)
    public ResponseEntity<Void> nonExistResourceExceptionHandler(RuntimeException e) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler()
    public ResponseEntity<Void> handleIllegalArgsException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().build();
    }

}
