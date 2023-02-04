package subway.line.presentation;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.common.exception.ErrorResponse;
import subway.line.exception.LineNotFoundException;
import subway.section.exception.DownStationAlreadyRegisteredException;
import subway.section.exception.DownStationNotFoundException;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@RestControllerAdvice
@Order(HIGHEST_PRECEDENCE)
public class LineControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(LineNotFoundException.class)
    public ErrorResponse handleLineNotFound(final LineNotFoundException exception) {
        return ErrorResponse.from(exception);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DownStationNotFoundException.class)
    public ErrorResponse handleDownStationNotFound(final DownStationNotFoundException exception) {
        return ErrorResponse.from(exception);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DownStationAlreadyRegisteredException.class)
    public ErrorResponse handleDownStationAlreadyRegistered(final DownStationAlreadyRegisteredException exception) {
        return ErrorResponse.from(exception);
    }
}
