package subway.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ErrorController {
    @ExceptionHandler(value = NoSuchElementException.class)
    public ResponseEntity<ApiError> handleException(Exception e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError(e.getMessage(), HttpStatus.NOT_FOUND.value()));
    }
}
