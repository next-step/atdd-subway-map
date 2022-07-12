package nextstep.subway.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRunTime(RuntimeException exception) {
        log.error("[" + exception.getClass().getName() + "] " + exception.getMessage());
        return ResponseEntity.internalServerError().body(exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException exception) {
        log.error("[" + exception.getClass().getName() + "] " + exception.getMessage());
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
}
