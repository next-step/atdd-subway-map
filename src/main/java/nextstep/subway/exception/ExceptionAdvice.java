package nextstep.subway.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class ExceptionAdvice {

    private Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(EntityExistsException.class)
    protected ResponseEntity<ErrorResponse> handleEntityExistsException(final EntityExistsException entityExistsException) {
        final String errorMessage = entityExistsException.getMessage();
        log.error("EntityExistsException : {}", errorMessage);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ErrorResponse(errorMessage));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleEntityNotFoundException(final EntityNotFoundException entityNotFoundException) {
        final String errorMessage = entityNotFoundException.getMessage();
        log.error("EntityNotFoundException : {}", errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(errorMessage));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<ErrorResponse> handleIllegalArgumentException(final IllegalArgumentException illegalArgumentException) {
        final String errorMessage = illegalArgumentException.getMessage();
        log.error("IllegalArgumentException : {}", errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(errorMessage));
    }
}
