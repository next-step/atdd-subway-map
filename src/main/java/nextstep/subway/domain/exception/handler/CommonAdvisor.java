package nextstep.subway.domain.exception.handler;

import nextstep.subway.domain.exception.exception.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonAdvisor {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Void> exception(EntityNotFoundException e) {
        return ResponseEntity.badRequest().build();
    }
}
