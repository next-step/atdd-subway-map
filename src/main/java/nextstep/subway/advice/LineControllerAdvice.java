package nextstep.subway.advice;

import nextstep.subway.exception.AddSectionException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LineControllerAdvice {

    @ExceptionHandler(AddSectionException.class)
    public ResponseEntity<ErrorResult> duplicateStationException(AddSectionException e) {
        return ResponseEntity.badRequest().body(new ErrorResult(e.getMessage()));
    }
}
