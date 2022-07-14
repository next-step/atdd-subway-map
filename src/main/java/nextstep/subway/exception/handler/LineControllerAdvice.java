package nextstep.subway.exception.handler;

import nextstep.subway.exception.unchecked.SectionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LineControllerAdvice {

    @ExceptionHandler
    public ResponseEntity<Void> addSectionExceptionHandler(SectionException e) {
        return ResponseEntity.badRequest().build();
    }
}
