package nextstep.subway.ui.common;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResult entityNotFoundExHandler(EntityNotFoundException e) {
        return ErrorResult.builder()
                .errorCode(HttpStatus.NOT_FOUND.value())
                .errorMessage(e.getMessage())
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({IllegalArgumentException.class})
    public ErrorResult illegalExHandler(IllegalArgumentException e) {
        return ErrorResult.builder()
                .errorCode(HttpStatus.BAD_REQUEST.value())
                .errorMessage(e.getMessage())
                .build();
    }
}
