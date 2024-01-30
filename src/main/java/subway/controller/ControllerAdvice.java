package subway.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.exception.SectionAddFailureException;
import subway.exception.SectionDeleteFailureException;
import subway.global.ErrorResponse;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler({
        SectionAddFailureException.class, SectionDeleteFailureException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleSectionAddFailureException(RuntimeException e) {
        return ErrorResponse.of(e.getMessage());
    }
}
