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
import subway.section.exception.NotLastSectionException;
import subway.section.exception.SectionNotFoundException;
import subway.section.exception.SingleSectionException;

import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

@RestControllerAdvice
@Order(HIGHEST_PRECEDENCE)
public class LineControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(LineNotFoundException.class)
    ErrorResponse handleLineNotFound(final LineNotFoundException exception) {
        return ErrorResponse.from(exception);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DownStationNotFoundException.class)
    ErrorResponse handleDownStationNotFound(final DownStationNotFoundException exception) {
        return ErrorResponse.from(exception);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DownStationAlreadyRegisteredException.class)
    ErrorResponse handleDownStationAlreadyRegistered(final DownStationAlreadyRegisteredException exception) {
        return ErrorResponse.from(exception);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(SingleSectionException.class)
    ErrorResponse handleSingleSection(final SingleSectionException exception) {
        return ErrorResponse.from(exception);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(NotLastSectionException.class)
    ErrorResponse handleNotLastSection(final NotLastSectionException exception) {
        return ErrorResponse.from(exception);
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(SectionNotFoundException.class)
    ErrorResponse handleSectionNotFound(final SectionNotFoundException exception) {
        return ErrorResponse.from(exception);
    }
}
