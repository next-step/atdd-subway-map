package nextstep.subway.advice;

import nextstep.subway.exception.NotExistsStationIdException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SubwayAdvice {
    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity handleIllegalArgsException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity handleIllegalArgsException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler({NotExistsStationIdException.class})
    public ResponseEntity handleNotExistsStationIdException(NotExistsStationIdException e) {
        return ResponseEntity.badRequest().build();
    }
}
