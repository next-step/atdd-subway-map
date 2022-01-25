package nextstep.subway.ui.exception;

import nextstep.subway.applicaion.exception.DuplicationNameException;
import nextstep.subway.applicaion.exception.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DuplicationNameException.class)
    public ResponseEntity<String> duplicateNameException(DuplicationNameException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(exception.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> entityNotFoundException(EntityNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(exception.getMessage());

    }

}
