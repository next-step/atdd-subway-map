package subway.config;

import lombok.extern.slf4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> runtimeException(final RuntimeException e) {

        log.error("error: ", e);
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(e.getMessage()));
    }
}
