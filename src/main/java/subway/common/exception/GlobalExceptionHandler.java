package subway.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SubwayException.class)
    protected ResponseEntity<?> handleSubwayException(final SubwayException e) {
        final ErrorResponse errorResponse = ErrorResponse.of(e.getMessage(), e.getStatus());
        return ResponseEntity.status(e.getStatus()).body(errorResponse);
    }
}
