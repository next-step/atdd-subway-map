package subway.line.presentation;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.common.exception.ErrorResponse;
import subway.line.exception.LineNotFoundException;
import subway.line.exception.DownStationAlreadyRegisteredException;
import subway.line.exception.NotSameAsRegisteredDownStation;
import subway.line.exception.NotLastSectionException;
import subway.line.exception.SectionNotFoundException;
import subway.line.exception.SingleSectionException;

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
    @ExceptionHandler(NotSameAsRegisteredDownStation.class)
    ErrorResponse handleDownStationNotFound(final NotSameAsRegisteredDownStation exception) {
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
