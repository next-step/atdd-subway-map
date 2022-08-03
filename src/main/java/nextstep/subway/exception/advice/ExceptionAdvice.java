package nextstep.subway.exception.advice;

import nextstep.subway.applicaion.dto.ErrorResponse;
import nextstep.subway.exception.NotFoundException;
import nextstep.subway.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice(basePackages = {"nextstep.subway"})
public class ExceptionAdvice {

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<ErrorResponse> handleValidationException(Exception e) {
        return createErrorResponse(e, BAD_REQUEST);
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundException(Exception e) {
        return createErrorResponse(e, NOT_FOUND);
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(Exception e, HttpStatus httpStatus) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), httpStatus);
    }
}
