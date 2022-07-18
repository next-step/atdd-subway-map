package nextstep.subway.line.web;

import nextstep.subway.line.domain.exception.CannotAppendSectionException;
import nextstep.subway.line.domain.exception.CannotDeleteSectionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SectionExceptionHandler {

    @ExceptionHandler({CannotAppendSectionException.class, CannotDeleteSectionException.class})
    public ResponseEntity<Void> sectionException(RuntimeException e) {
        return ResponseEntity.badRequest().build();
    }
}
