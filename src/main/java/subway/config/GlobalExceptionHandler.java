package subway.config;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> runtimeException(final RuntimeException e) {

        return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getMessage()));
    }
}
