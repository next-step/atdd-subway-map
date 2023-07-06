package subway.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<ExceptionResponse> badRequest(SubwayException subwayException) {
        return ResponseEntity.badRequest()
                .body(new ExceptionResponse("fail", subwayException.getMessage()));
    }
}
