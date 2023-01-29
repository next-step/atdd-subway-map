package subway.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.common.exception.AlreadyExistException;
import subway.common.exception.NoDeleteOneSectionException;
import subway.common.exception.NoRegisterSectionException;
import subway.common.exception.NoRegisterStationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AlreadyExistException.class)
    protected ResponseEntity<ErrorResponse> handleAlreadyExistException(AlreadyExistException e) {
        final ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(NoRegisterSectionException.class)
    protected ResponseEntity<ErrorResponse> handleNoRegisterSectionException(NoRegisterSectionException e) {
        final ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(NoRegisterStationException.class)
    protected ResponseEntity<ErrorResponse> handleNoRegisterStationException(NoRegisterStationException e) {
        final ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(NoDeleteOneSectionException.class)
    protected ResponseEntity<ErrorResponse> handleNoDeleteOneSectionException(NoDeleteOneSectionException e) {
        final ErrorResponse errorResponse = new ErrorResponse(e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}
