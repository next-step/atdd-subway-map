package nextstep.subway.common.ui;

import javax.persistence.EntityNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import nextstep.subway.common.exception.DuplicateColumnException;
import nextstep.subway.common.exception.ErrorMessage;
import nextstep.subway.common.exception.ErrorResponse;

@ControllerAdvice
@RestController
public class ExceptionControllerAdvice {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResponse entityNotFound() {
        return new ErrorResponse(ErrorMessage.ENTITY_NOT_FOUND.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateColumnException.class)
    public ErrorResponse duplicateColumn(Exception e) {
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse invalidArgumentException(Exception e) {
        return new ErrorResponse(e.getMessage());
    }
}
