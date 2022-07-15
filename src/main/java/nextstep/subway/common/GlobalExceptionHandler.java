package nextstep.subway.common;

import nextstep.subway.applicaion.dto.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<ErrorResponse> noSuchElementException(NoSuchElementException e) {
        final ErrorResponse errorResponse = new ErrorResponse(NoSuchElementException.class.getName(), e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
