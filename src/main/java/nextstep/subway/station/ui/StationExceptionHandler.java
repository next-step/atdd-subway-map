package nextstep.subway.station.ui;

import nextstep.subway.station.exception.StationNameDuplicatedException;
import nextstep.subway.station.exception.StationNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerAdvice
public class StationExceptionHandler {

    @ExceptionHandler(StationNotFoundException.class)
    public void handle(HttpServletResponse response, StationNotFoundException e) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(StationNameDuplicatedException.class)
    public void handle(HttpServletResponse response, StationNameDuplicatedException e) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }
}
