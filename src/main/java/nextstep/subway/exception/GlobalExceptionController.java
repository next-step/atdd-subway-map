package nextstep.subway.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(ExistDownStationException.class)
    public ResponseEntity handleNotValidDownStationException(ExistDownStationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(CanNotMatchUpStationException.class)
    public ResponseEntity handleNotValidDownStationException(CanNotMatchUpStationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
