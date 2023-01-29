package subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * GlobalRestControllerAdvice
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 01. 29.
 */
@RestControllerAdvice
public class GlobalRestControllerAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity notFoundException() {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity badRequestException() {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
