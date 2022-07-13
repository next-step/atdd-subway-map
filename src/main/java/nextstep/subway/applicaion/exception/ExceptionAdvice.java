package nextstep.subway.applicaion.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ExceptionResponse> exceptionHandler(IllegalArgumentException e) {
        return ResponseEntity.internalServerError().body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<ExceptionResponse> exceptionHandler(RuntimeException e) {
        return ResponseEntity.internalServerError().body(new ExceptionResponse(e.getMessage()));
    }
}
