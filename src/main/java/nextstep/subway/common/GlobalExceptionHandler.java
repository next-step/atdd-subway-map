package nextstep.subway.common;

import nextstep.subway.exception.DuplicatedException;
import nextstep.subway.exception.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DuplicatedException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatedLineException(DuplicatedException ex) {
        ErrorResponse body = new ErrorResponse(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(body);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse body = new ErrorResponse(ex.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(body);
    }

}
