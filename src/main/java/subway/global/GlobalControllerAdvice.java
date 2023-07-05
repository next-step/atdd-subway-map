package subway.global;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalControllerAdvice {

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<ExceptionResponse> handleException(final SubwayException e) {
        return ResponseEntity
                .badRequest()
                .body(new ExceptionResponse(e.getMessage()));
    }
}
