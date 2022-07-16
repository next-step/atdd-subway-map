package nextstep.subway.ui;

import nextstep.subway.domain.exception.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> exception(EntityNotFoundException e) {
        return ResponseEntity.badRequest().build();
    }
}
