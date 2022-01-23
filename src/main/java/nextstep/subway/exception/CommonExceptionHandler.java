package nextstep.subway.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CommonExceptionHandler {

    @ExceptionHandler(DuplicatedElementException.class)
    public ResponseEntity handleDuplicatedElementExceptionException(DuplicatedElementException e) {
        return ResponseEntity.badRequest().build();
    }

}
