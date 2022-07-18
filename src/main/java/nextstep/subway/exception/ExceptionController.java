package nextstep.subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Objects;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodValidException(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(makeErrorResponse(e.getBindingResult()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SubwayException.class)
    public ResponseEntity<ErrorResponse> subwayException(SubwayException e) {
        return new ResponseEntity<>(makeErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    private ErrorResponse makeErrorResponse(BindingResult bindingResult) {
        String detail = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), detail);
    }

    private ErrorResponse makeErrorResponse(String message) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message);
    }
}
