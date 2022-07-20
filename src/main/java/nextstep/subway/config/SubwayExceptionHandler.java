package nextstep.subway.config;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class SubwayExceptionHandler {

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<ErrorResponse> entityNotFoundException(EntityNotFoundException e) {
        final ErrorResponse errorResponse = new ErrorResponse(EntityNotFoundException.class.getName(), e.getMessage());
        return ResponseEntity.badRequest()
                .body(errorResponse);
    }

    @ExceptionHandler({EmptyResultDataAccessException.class})
    public ResponseEntity<ErrorResponse> emptyResultDataAccessException(EmptyResultDataAccessException e) {
        final ErrorResponse errorResponse = new ErrorResponse(EmptyResultDataAccessException.class.getName(), e.getMessage());
        return ResponseEntity.badRequest()
                .body(errorResponse);
    }

}