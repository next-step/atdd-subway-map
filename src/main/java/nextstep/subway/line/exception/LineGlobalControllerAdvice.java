package nextstep.subway.line.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LineGlobalControllerAdvice {
    @ExceptionHandler(DuplicateStationInLineException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleDuplicateStationLineException() {
        return "DuplicateStationInLineException";
    }

    @ExceptionHandler(NonExistStationInLineException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleNonExistStationException() {
        return "NonExistStationInLineException";
    }
}
