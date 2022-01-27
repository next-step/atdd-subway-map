package nextstep.subway.exception;

import nextstep.subway.applicaion.dto.ExceptionResponse;
import nextstep.subway.exception.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> runtimeExceptionHandler(RuntimeException e) {
        e.printStackTrace();
        return ResponseEntity.internalServerError().body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ExceptionResponse> badRequestExceptionHandler(BadRequestException e) {
        e.printStackTrace();
        return ResponseEntity.badRequest().body(new ExceptionResponse(e.getMessage()));
    }
}
