package nextstep.subway.ui;

import nextstep.subway.exception.DuplicatedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.security.InvalidParameterException;

@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler({EntityNotFoundException.class, InvalidParameterException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<Void> getUnprocessableEntityStateEntity() {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY.value()).build();
    }

    @ExceptionHandler(DuplicatedException.class)
    public ResponseEntity<Void> getConflictEntity() {
        return ResponseEntity.status(HttpStatus.CONFLICT.value()).build();
    }
}
