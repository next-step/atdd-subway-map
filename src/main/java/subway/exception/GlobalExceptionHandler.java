package subway.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<Object> handleBadRequestException(SubwayException subwayException) {
        ErrorResponse errorResponse = new ErrorResponse(subwayException.getErrorCode().getStatus(), subwayException.getErrorCode().getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
