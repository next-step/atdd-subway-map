package nextstep.subway.ui.advice;

import nextstep.subway.applicaion.exception.NotFoundException;
import nextstep.subway.applicaion.exception.NameDuplicatedException;
import nextstep.subway.ui.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(NameDuplicatedException.class)
    public ErrorResponse handleNameDuplicatedException(NameDuplicatedException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorResponse handleLineNotFoundException(NotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }
}
