package nextstep.subway.common.ui;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.common.exception.DuplicateColumnException;
import nextstep.subway.common.exception.EntityNotFoundException;
import nextstep.subway.common.exception.ErrorResponse;

@ControllerAdvice
@RestController
public class ExceptionControllerAdvice {
    @ResponseStatus(HttpStatus.NOT_IMPLEMENTED)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResponse entityNotFound(Exception e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateColumnException.class)
    public ErrorResponse duplicateColumn(Exception e) {
        return new ErrorResponse(e.getMessage());
    }
}
