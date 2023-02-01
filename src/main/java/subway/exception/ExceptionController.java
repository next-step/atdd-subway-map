package subway.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ExceptionController {
    @ExceptionHandler(value = {
        NotFoundLineException.class,
        NotFoundStationException.class,
        NotFoundLineSectionException.class,
        NotFoundSectionException.class
    })
    public ResponseEntity<?> notFoundException(Exception e) {
        log.info("Not Found Exception: {}", e.getMessage(), e);

        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<?> badRequestException(Exception e) {
        log.info("Bad Request Exception: {}", e.getMessage(), e);

        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<?> exception(Exception e) {
        log.error("Unexpected Exception: {}", e.getMessage(), e);

        return ResponseEntity.internalServerError().build();
    }
}
