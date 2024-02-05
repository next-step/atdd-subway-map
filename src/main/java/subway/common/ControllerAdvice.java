package subway.common;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handlerIllegalArgumentException(final IllegalArgumentException e) {
        return ResponseEntity.status(BAD_REQUEST)
                             .body(new ErrorResponse(BAD_REQUEST.name(), e.getMessage()));
    }
}
