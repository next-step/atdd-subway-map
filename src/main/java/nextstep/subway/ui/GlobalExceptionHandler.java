package nextstep.subway.ui;

import nextstep.subway.domain.service.DuplicateArgumentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

    @ExceptionHandler(DuplicateArgumentException.class)
    protected ResponseEntity<Void> handleDuplicateArgumentException(DuplicateArgumentException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
