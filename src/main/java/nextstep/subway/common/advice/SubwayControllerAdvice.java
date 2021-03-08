package nextstep.subway.common.advice;

import nextstep.subway.common.exception.ApplicationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SubwayControllerAdvice {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity handleIllegalArgsException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity handleIllegalArgsException(ApplicationException e) {
        return new ResponseEntity(e.getStatus());
    }
}
