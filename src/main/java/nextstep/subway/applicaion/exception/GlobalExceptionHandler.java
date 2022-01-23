package nextstep.subway.applicaion.exception;

import nextstep.subway.applicaion.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<ErrorResponse> duplicateException(DuplicateException e) {

        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), HttpStatus.valueOf(e.getCode().value()));
    }
}
